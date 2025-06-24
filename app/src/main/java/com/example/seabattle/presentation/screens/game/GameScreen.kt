package com.example.seabattle.presentation.screens.game


import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.seabattle.R
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.presentation.screens.Screen
import com.example.seabattle.presentation.screens.game.resources.ReadyCheckSection
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.platform.LocalContext
import com.example.seabattle.data.local.gameSample1
import com.example.seabattle.presentation.screens.game.resources.GameSection
import com.example.seabattle.presentation.screens.game.resources.PlayersInfoHeader
import com.example.seabattle.presentation.screens.game.resources.WaitGameSection

@Composable
fun GameScreen(
    modifier: Modifier,
    navController: NavHostController,
    gameViewModel: GameViewModel = koinViewModel(),
) {
    val gameUiState by gameViewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showLeaveDialog by remember { mutableStateOf(false) }

    // Stop listeners when the screen is disposed
    DisposableEffect(Unit) {
        gameViewModel.startListeningGame()
        onDispose {
            gameViewModel.stopListening()
        }
    }

    // Show a toast message when an error occurs
    LaunchedEffect(key1 = gameUiState.errorMessage) {
        gameUiState.errorMessage?.let { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            gameViewModel.onErrorShown()
        }
    }


    // If the user presses the back button, show a confirmation dialog
    BackHandler(
        onBack = { showLeaveDialog = true }
    )

    // Observe the game state and navigate to the home screen if the game has been deleted or aborted
    LaunchedEffect(key1 = gameUiState.game) {
        val game = gameUiState.game
        if (game!= null && game.gameState == GameState.GAME_ABORTED.name) {
            gameViewModel.onUserLeave()
            navController.navigate(Screen.Home.title){
                popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
            }
        }
    }


    GameScreenContent(
        modifier = modifier,
        game = gameUiState.game,
        userId = gameUiState.userId,
        onClickReady = gameViewModel::onClickReady,
        enableReadyButton = gameViewModel::enableReadyButton,
        onClickLeave = { showLeaveDialog = true },
        onClickCell = gameViewModel::onClickCell,
        enableClickCell = gameViewModel::enableClickCell,
        enableSeeShips = gameViewModel::enableSeeShips
    )

    // Show a dialog to confirm the user wants to leave the game
    if (showLeaveDialog) {
        AlertDialog(
            onDismissRequest = {
                showLeaveDialog = false
            },
            title = {
                Text("Confirm you want to leave")
            },
            text = {
                Text("¿Are you sure you want to leave?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLeaveDialog = false
                        gameViewModel.onUserLeave()
                        navController.navigate(Screen.Home.title){
                            popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                        }
                    }
                ) {
                    Text("Leave")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showLeaveDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }


    // Show a dialog to let
    if (gameUiState.showClaimDialog) {
        AlertDialog(
            onDismissRequest = {
                gameViewModel.onDismissClaimDialog()
            },
            title = {
                Text("Opponent is AFK")
            },
            text = {
                Text("¿Do you want to claim victory?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        gameViewModel.onClaimVictory()
                    }
                ) {
                    Text("Claim Victory")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        gameViewModel.onDismissClaimDialog()
                    }
                ) {
                    Text("Continue Playing")
                }
            }
        )
    }
}

@Composable
fun GameScreenContent(
    modifier: Modifier,
    game: Game?,
    userId: String,
    onClickReady: () -> Unit = {},
    enableReadyButton: () -> Boolean = { true },
    onClickLeave: () -> Unit = {},
    onClickCell: (row: Int, col: Int) -> Unit = { _, _ -> },
    enableClickCell: (gameBoardOwner: String) -> Boolean = { true },
    enableSeeShips: (watcher: String) -> Boolean = { false }
) {
    if (game == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading game data...")
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_medium))
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        item {
            PlayersInfoHeader(
                modifier = modifier,
                player1 = game.player1,
                player2 = game.player2
            )
        }

        // Screen waiting for players to be ready
        if (game.gameState == GameState.WAITING_FOR_PLAYER.name) {
            item {
                WaitGameSection(
                    modifier = modifier.fillMaxSize(),
                    game = game,
                    onClickLeave = onClickLeave
                )
            }
        }



        // Screen waiting for players to be ready
        if (game.gameState == GameState.CHECK_READY.name) {
            item {
                ReadyCheckSection(
                    modifier = modifier.fillMaxSize(),
                    game = game,
                    onClickReady = onClickReady,
                    enableReadyButton = enableReadyButton(),
                    onClickLeave = onClickLeave
                )
            }
        }

        // During the game, each player can click on the opponent's board to make a move
        // During the game, each player can click on the opponent's board to make a move
        if (game.gameState == GameState.IN_PROGRESS.name) {
            item {
                GameSection(
                    modifier = modifier.fillMaxSize(),
                    game = game,
                    userId = userId,
                    onClickCell = onClickCell,
                    enableClickCell = enableClickCell,
                    enableSeeShips = enableSeeShips,
                )
            }
        } else if (game.gameState == GameState.GAME_FINISHED.name) {
            // Screen showing the case when the opponent has left the game and the user is the winner
            item {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if(game.winnerId == userId) "You have won!!" else "You have lost!",
                        fontSize = 24.sp,
                        fontWeight = SemiBold,
                        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small))
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GameScreenPreview(){
    GameScreenContent(
        modifier = Modifier.fillMaxSize(),
        game = gameSample1,
        userId = gameSample1.player1.userId
    )
}
