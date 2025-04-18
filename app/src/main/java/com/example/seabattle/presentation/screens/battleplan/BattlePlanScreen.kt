package com.example.seabattle.presentation.screens.battleplan

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BattlePlanScreen(
    modifier: Modifier = Modifier
) {
    // TO DO
    Column(
        modifier = modifier
    ) {
        Text(
            text = "BattlePlan Screen",
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