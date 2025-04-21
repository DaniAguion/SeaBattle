package com.example.seabattle.presentation.screens.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.seabattle.domain.model.GameBoard
import com.example.seabattle.presentation.resources.Board

@Composable
fun GameScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "GameScreen Screen"
        )
        Board(
            gameBoard = GameBoard(),
            onCellClick = {}
        )
    }
}


@Preview
@Composable
fun GameScreenPreview(){
    GameScreen(
        modifier = Modifier
    )
}