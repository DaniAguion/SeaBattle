package com.example.seabattle.presentation.screens.battleplan

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.seabattle.domain.model.GameBoard
import com.example.seabattle.presentation.resources.Board

@Composable
fun BattlePlanScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "BattlePlan Screen"
        )
        Board(
            gameBoard = GameBoard(),
            onCellClick = {}
        )
    }
}


@Preview
@Composable
fun BattlePlanPreview(){
    BattlePlanScreen(
        modifier = Modifier
    )
}