package com.example.seabattle.presentation.screens.game.resources

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.seabattle.R
import com.example.seabattle.data.local.gameSample1
import com.example.seabattle.domain.entity.Game


@Composable
fun WaitGameSection(
    game: Game,
    onClickLeave: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.padding_medium))
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        // Header
        Text(
            text = "Waiting Player",
            fontSize = 20.sp,
            fontWeight = SemiBold,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        )
        Text(
            text = "Game Name: ${game.gameName}",
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        )
        Text(
            text = "Player 1: ${game.player1.displayName}",
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        )
        if (game.player2.userId == "") {
            Text(
                text = "Player 2: ${game.player2.displayName}",
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            )
        }
        if (game.player2.userId == "") {
            Text(
                text = "Waiting for player 2...",
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_big))
            )
            Button(
                onClick = onClickLeave,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            ) {
                Text(text = "Leave Game")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun WaitGameSectionPreview(){
    WaitGameSection(
        game = gameSample1,
        onClickLeave = {}
    )
}