package com.example.seabattle.presentation.screens.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.seabattle.R
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameBoard
import com.example.seabattle.domain.entity.Room
import com.example.seabattle.presentation.resources.Board
import com.example.seabattle.presentation.screens.room.RoomViewModel
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


/*
@Preview(showBackground = true)
@Composable
fun GameScreenPreview(){
    GameScreenContent(
        modifier = Modifier.fillMaxSize(),
        gameBoard = GameBoard(),
        onCellClick = { x, y -> }
    )
}
*/