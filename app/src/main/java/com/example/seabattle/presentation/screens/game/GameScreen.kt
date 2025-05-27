package com.example.seabattle.presentation.screens.game

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.seabattle.R
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameBoard
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.entity.UserBasic
import com.example.seabattle.presentation.SeaBattleScreen
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun GameScreen(
    modifier: Modifier,
    navController: NavHostController,
    gameViewModel: GameViewModel = koinViewModel(),
) {
    val gameUiState by gameViewModel.uiState.collectAsState()


    // Stop listeners when the screen is disposed
    DisposableEffect(Unit) {
        onDispose {
            gameViewModel.stopListening()
        }
    }

    // If the user presses the back button, leave the game and navigate to the home screen
    BackHandler(
        onBack = {
            gameViewModel.onUserLeave()
            navController.navigate(SeaBattleScreen.Home.title){
                popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
            }
        }
    )

    // Observe the game state and navigate to the home screen if the game has ended
    LaunchedEffect(key1 = gameUiState.game) {
        val game = gameUiState.game
        if (game!= null && game.gameState == GameState.GAME_ABORTED.name) {
            gameViewModel.onUserLeave()
            navController.navigate(SeaBattleScreen.Home.title){
                popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
            }
        }
    }


    GameScreenContent(
        modifier = modifier,
        game = gameUiState.game,
        onClickReady = gameViewModel::onClickReady,
        enableReadyButton = gameViewModel::enableReadyButton,
        onUserLeave = gameViewModel::onUserLeave
    )
}

@Composable
fun GameScreenContent(
    modifier: Modifier,
    game: Game?,
    onClickReady: () -> Unit = {},
    enableReadyButton: () -> Boolean = { true },
    onUserLeave: () -> Unit = {}
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
        if (game.gameState == GameState.CHECK_READY.name) {
            item {
                ReadyCheckSection(
                    game = game,
                    onClickReady = onClickReady,
                    enableReadyButton = enableReadyButton(),
                    onClickLeave = onUserLeave
                )
            }
        }
    }
}

@Composable
fun PlayersInfoHeader(
    modifier: Modifier,
    player1: UserBasic,
    player2: UserBasic
) {
    Row(
        modifier = modifier
            .padding(bottom = dimensionResource(R.dimen.padding_big)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        CardPlayer(player = player1)
        CardPlayer(player = player2)
    }
}


@Composable
fun CardPlayer(
    player: UserBasic,
){
    Card {
        Row(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_small))
                .sizeIn(
                    minWidth = 150.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ){
            if (player.photoUrl.isEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.account_box_40px),
                    contentDescription = "User photo",
                    modifier = Modifier.size(40.dp)
                )
            } else {
                AsyncImage(
                    model = player.photoUrl,
                    contentDescription = "User photo",
                    modifier = Modifier.size(40.dp),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.account_box_40px),
                )
            }

            Text(
                text = player.displayName,
                fontSize = 16.sp,
                fontWeight = SemiBold,
                modifier = Modifier
                    .padding(start = dimensionResource(R.dimen.padding_small))
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GameScreenPreview(){
    GameScreenContent(
        modifier = Modifier.fillMaxSize(),
        game = Game(
            gameId = "123",
            player1 = UserBasic("userId", "Daniel"),
            player1Board = GameBoard().toMapOfMaps(),
            player2 = UserBasic("userId", "PedroPablo80"),
            player2Board = GameBoard().toMapOfMaps(),
            gameState = GameState.CHECK_READY.name
        )
    )
}
