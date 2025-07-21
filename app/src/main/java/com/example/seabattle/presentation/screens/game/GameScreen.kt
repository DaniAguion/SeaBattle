package com.example.seabattle.presentation.screens.game


import com.example.seabattle.R
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.presentation.screens.Screen
import com.example.seabattle.presentation.screens.game.resources.ReadyCheckSection
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.seabattle.data.local.sampleGame
import com.example.seabattle.presentation.resources.toErrorMessageUI
import com.example.seabattle.presentation.screens.game.resources.GameFinishedSection
import com.example.seabattle.presentation.screens.game.resources.GameSection
import com.example.seabattle.presentation.screens.game.resources.PlayersInfoHeader
import com.example.seabattle.presentation.screens.game.resources.WaitGameSection
import com.example.seabattle.presentation.theme.SeaBattleTheme

@Composable
fun GameScreen(
    modifier: Modifier,
    navController: NavHostController,
    gameViewModel: GameViewModel = koinViewModel(),
) {
    val gameUiState by gameViewModel.uiState.collectAsState()
    val soundMutedState by gameViewModel.isMuted.collectAsState()
    val context = LocalContext.current

    // Get the current back stack entry to determine the current route
    // This its used to clean the back stack when navigating to the home screen
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route ?: Screen.Game.name

    var showLeaveDialog by remember { mutableStateOf(false) }


    // Stop listeners when the screen is disposed
    DisposableEffect(Unit) {
        gameViewModel.startListeningGame()
        onDispose {
            gameViewModel.stopListening()
        }
    }


    // Show a toast message when an error occurs
    LaunchedEffect(key1 = gameUiState.error) {
        gameUiState.error?.let { error ->
            Toast.makeText(context, context.getString(error.toErrorMessageUI()), Toast.LENGTH_LONG).show()
            gameViewModel.onErrorShown()
        }
    }


    // If the user presses the back button, show a confirmation dialog
    BackHandler(
        onBack = { showLeaveDialog = true }
    )

    // Observe the game state and call onUserLeave if the game was aborted
    LaunchedEffect(key1 = gameUiState.game) {
        if (gameUiState.game?.gameState == GameState.GAME_ABORTED.name) {
            gameViewModel.onUserLeave()
        }
    }

    // If the user has left the game, navigate to the home screen and clear the back stack
    LaunchedEffect(key1 = gameUiState.hasLeftGame) {
        if (gameUiState.hasLeftGame) {
            navController.navigate(Screen.Home.title){
                popUpTo(currentRoute) { inclusive = true }
            }
        }
    }

    // Show a dialog to confirm the user wants to leave the game
    if (showLeaveDialog) {
        AlertDialog(
            onDismissRequest = {
                showLeaveDialog = false
            },
            title = {
                Text(stringResource(R.string.leave_dialog_title))
            },
            text = {
                Text(stringResource(R.string.leave_dialog_desc))
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLeaveDialog = false
                        gameViewModel.onUserLeave()
                        navController.navigate(Screen.Home.title) {
                            popUpTo(currentRoute) { inclusive = true }
                        }
                    }
                ) {
                    Text(stringResource(R.string.leave_dialog_confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showLeaveDialog = false
                    }
                ) {
                    Text(stringResource(R.string.leave_dialog_cancel))
                }
            }
        )
    }


    if (gameUiState.showClaimDialog) {
        AlertDialog(
            onDismissRequest = {
                gameViewModel.onDismissClaimDialog()
            },
            title = {
                Text(stringResource(R.string.claim_dialog_title))
            },
            text = {
                Text(stringResource(R.string.claim_dialog_desc))
            },
            confirmButton = {
                Button(
                    onClick = {
                        gameViewModel.onClaimVictory()
                    }
                ) {
                    Text(stringResource(R.string.claim_dialog_confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        gameViewModel.onDismissClaimDialog()
                    }
                ) {
                    Text(stringResource(R.string.claim_dialog_cancel))
                }
            }
        )
    }

    GameScreenContent(
        modifier = modifier,
        soundMuted = soundMutedState,
        game = gameUiState.game,
        userId = gameUiState.userId,
        userScore = gameUiState.userScore,
        onClickReady = gameViewModel::onClickReady,
        enableReadyButton = gameViewModel::enableReadyButton,
        onClickLeave = { showLeaveDialog = true },
        onClickCell = gameViewModel::onClickCell,
        enableClickCell = gameViewModel::enableClickCell,
        enableSeeShips = gameViewModel::enableSeeShips,
        toggleMute = gameViewModel::toggleMute
    )
}

@Composable
fun GameScreenContent(
    modifier: Modifier,
    soundMuted: Boolean,
    game: Game?,
    userId: String,
    userScore: Int,
    onClickReady: () -> Unit = {},
    enableReadyButton: () -> Boolean = { true },
    onClickLeave: () -> Unit = {},
    onClickCell: (row: Int, col: Int) -> Unit = { _, _ -> },
    enableClickCell: (gameBoardOwner: String) -> Boolean = { true },
    enableSeeShips: (watcher: String) -> Boolean = { false },
    toggleMute: () -> Unit = {}
) {
    if (game == null) {
        Box(
            modifier = Modifier.fillMaxSize()
            , contentAlignment = Alignment.Center
        ) {
            // Empty state when the game is null
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        item {
            PlayersInfoHeader(
                player1 = game.player1,
                player2 = game.player2,
                modifier = Modifier
            )
        }

        item {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_xsmall))
            ) {
                IconButton(onClick = { toggleMute() }) {
                    Icon(
                        imageVector = if (soundMuted) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = if (soundMuted) stringResource(R.string.unmute_sound) else stringResource(R.string.mute_sound),
                        tint = if (soundMuted) Color.Gray else Color.Unspecified
                    )
                }
            }
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
            item{
                GameFinishedSection(
                    modifier = modifier.fillMaxSize(),
                    game = game,
                    userId = userId,
                    userScore = userScore,
                    onClickLeave = onClickLeave
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GameScreenPreview(){
    SeaBattleTheme {
        GameScreenContent(
            modifier = Modifier.fillMaxSize(),
            soundMuted = false,
            game = sampleGame.copy(
                gameState = GameState.IN_PROGRESS.name,
            ),
            userId = sampleGame.player1.userId,
            userScore = 100,
        )
    }
}
