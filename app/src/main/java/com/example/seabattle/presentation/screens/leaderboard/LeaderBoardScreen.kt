package com.example.seabattle.presentation.screens.leaderboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LeaderBoardScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Leader Board Screen"
        )
    }
}


@Preview(showBackground = true)
@Composable
fun LeaderBoardPreview(){
    LeaderBoardScreen(
        modifier = Modifier.fillMaxSize(),
    )
}