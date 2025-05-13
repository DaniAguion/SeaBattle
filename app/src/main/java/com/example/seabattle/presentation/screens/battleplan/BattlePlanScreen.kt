package com.example.seabattle.presentation.screens.battleplan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

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
    }
}


@Preview(showBackground = true)
@Composable
fun BattlePlanPreview(){
    BattlePlanScreen(
        modifier = Modifier.fillMaxSize(),
    )
}