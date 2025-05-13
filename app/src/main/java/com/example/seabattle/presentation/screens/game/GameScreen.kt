package com.example.seabattle.presentation.screens.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
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

    GameScreenContent(
        modifier = modifier,
        navController = navController,
        game = gameUiState.game
    )
}

@Composable
fun GameScreenContent(
    modifier: Modifier,
    navController: NavHostController,
    game: Game?
) {
    Column(
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_medium))
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_small)),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    AsyncImage(
                        model = game?.player1?.photoUrl,
                        contentDescription = "User photo",
                        modifier = Modifier.size(40.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "${game?.player1?.displayName}",
                        fontSize = 16.sp,
                        fontWeight = SemiBold,
                        modifier = Modifier
                            .padding(start = dimensionResource(R.dimen.padding_small))
                    )
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_small)),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    AsyncImage(
                        model = game?.player2?.photoUrl,
                        contentDescription = "User photo",
                        modifier = Modifier.size(40.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "${game?.player2?.displayName}",
                        fontSize = 16.sp,
                        fontWeight = SemiBold,
                        modifier = Modifier
                            .padding(start = dimensionResource(R.dimen.padding_small))
                    )
                }
            }
        }
        Text(
            text = "Game Screen",
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        )
        if (game != null) {
            Text(
                text = "Game ID: ${game.gameId}",
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            )
            Text(
                text = "State: ${game.gameState}",
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            )
            Text(
                text = "Player 1: ${game.player1.displayName}",
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            )
            Text(
                text = "Player 2: ${game.player2.displayName}",
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}

        /*
        Board(
            gameBoard = gameBoard,
            onCellClick = onCellClick,
        )
        */

@Composable
fun CardPlayer(){

}



@Preview(showBackground = true)
@Composable
fun GameScreenPreview(){
    GameScreenContent(
        modifier = Modifier.fillMaxSize(),
        navController = NavHostController(context = LocalContext.current),
        game = Game(
            gameId = "123",
            player1 = UserBasic("userId", "displayName"),
            player1Board = GameBoard().toMapOfMaps(),
            player2 = UserBasic("userId", "displayName"),
            player2Board = GameBoard().toMapOfMaps(),
            gameState = GameState.IN_PROGRESS.name
        )
    )
}
