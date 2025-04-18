package com.example.seabattle.presentation.screens.battleplan

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.seabattle.presentation.resources.GameBoard

@Composable
fun BattlePlanScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "BattlePlan Screen",
            modifier = modifier
        )
        GameBoard(
            modifier = modifier
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