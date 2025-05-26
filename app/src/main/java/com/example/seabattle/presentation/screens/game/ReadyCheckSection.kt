package com.example.seabattle.presentation.screens.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seabattle.R
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.UserBasic

@Composable
fun ReadyCheckSection(
    game: Game,
    onClickReady: () -> Unit = {},
    enableReadyButton : Boolean = true
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium))
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Confirm when you are ready!",
                fontSize = 20.sp,
                fontWeight = SemiBold,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
            )
            Text(
                text = "Waiting for players to be ready...",
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
            )
            Text(
                text = "${game.player1.displayName} - ${if (game.player1Ready) "Ready" else "Not Ready"}",
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
            )
            Text(
                text = "${game.player2.displayName} - ${if (game.player2Ready) "Ready" else "Not Ready"}",
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
            )
            Button(
                onClick = onClickReady,
                enabled = enableReadyButton,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
                    .sizeIn(minWidth = 150.dp)
            ) {
                Text(text = "Ready")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ReadyCheckSectionPreview(){
    ReadyCheckSection(
        game = Game(
            gameId = "123",
            player1 = UserBasic("userId", "Daniel"),
            player2 = UserBasic("userId", "PedroPablo80"),
        )
    )
}