package com.example.seabattle.presentation.screens.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.seabattle.presentation.resources.Board
import org.koin.androidx.compose.koinViewModel

@Composable
fun GameScreen(
    modifier: Modifier,
    gameViewModel: GameViewModel = koinViewModel(),
) {
    val gameUiState by gameViewModel.uiState.collectAsState()

    GameScreenContent(
        modifier = modifier,
        gameUiState = gameUiState,
        onCellClick = gameViewModel::onCellClick
    )
}

@Composable
fun GameScreenContent(
    modifier: Modifier,
    gameUiState: GameUiState,
    onCellClick: (Int, Int) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "GameScreen Screen"
        )
        Board(
            gameBoard = gameUiState.gameBoard,
            onCellClick = onCellClick,
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GameScreenPreview(){
    GameScreenContent(
        modifier = Modifier.fillMaxSize(),
        gameUiState = GameUiState(),
        onCellClick = { x, y -> }
    )
}