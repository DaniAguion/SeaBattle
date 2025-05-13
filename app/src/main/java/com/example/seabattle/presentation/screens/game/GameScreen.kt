package com.example.seabattle.presentation.screens.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.seabattle.R
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameBoard
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.entity.UserBasic
import org.koin.androidx.compose.koinViewModel

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

    GameScreenContent(
        modifier = modifier,
        game = gameUiState.game,
        onClickReady = gameViewModel::onClickReady
    )
}

@Composable
fun GameScreenContent(
    modifier: Modifier,
    game: Game?,
    onClickReady: () -> Unit = {},
) {
    LazyColumn(
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_medium))
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Players
        item {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(R.dimen.padding_big)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                CardPlayer(
                    player = game?.player1
                )
                CardPlayer(
                    player = game?.player2
                )
            }
        }

        // Screen waiting for players to be ready

        if (game?.gameState == GameState.CHECK_READY.name) {
            item {
                Text(
                    text = "Confirm when you are ready!",
                    fontSize = 20.sp,
                    fontWeight = SemiBold,
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_small))
                )
                Button(
                    onClick = onClickReady,
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_small))
                        .sizeIn(minWidth = 150.dp)
                ) {
                    Text(text = "Ready")
                }
            }
            item {
                if (game.player1Ready) {
                    Text(
                        text = "${game.player1.displayName} is ready",
                        fontSize = 16.sp,
                        fontWeight = SemiBold,
                        modifier = Modifier
                            .padding(dimensionResource(R.dimen.padding_small))
                    )
                } else {
                    Text(
                        text = "Waiting for ${game.player1.displayName} to be ready...",
                        fontSize = 16.sp,
                        fontWeight = SemiBold,
                        modifier = Modifier
                            .padding(dimensionResource(R.dimen.padding_small))
                    )
                }
                if (game.player2Ready) {
                    Text(
                        text = "${game.player2.displayName} is ready",
                        fontSize = 16.sp,
                        fontWeight = SemiBold,
                        modifier = Modifier
                            .padding(dimensionResource(R.dimen.padding_small))
                    )
                } else {
                    Text(
                        text = "Waiting for ${game.player2.displayName} to be ready...",
                        fontSize = 16.sp,
                        fontWeight = SemiBold,
                        modifier = Modifier
                            .padding(dimensionResource(R.dimen.padding_small))
                    )
                }
            }
        }
    }
}



@Composable
fun CardPlayer(
    player: UserBasic?,
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
            if (player?.photoUrl.isNullOrEmpty()) {
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
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = "${player?.displayName}",
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
