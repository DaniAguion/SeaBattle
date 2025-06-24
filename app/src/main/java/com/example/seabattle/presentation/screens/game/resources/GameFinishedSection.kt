package com.example.seabattle.presentation.screens.game.resources

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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
import com.example.seabattle.data.local.gameSample1
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.presentation.theme.SeaBattleTheme


@Composable
fun GameFinishedSection(
    modifier: Modifier = Modifier,
    game: Game,
    userId: String,
    userScore: Int,
    onClickLeave: () -> Unit = {}
) {

    val initialScore = if (userId == game.player1.userId) game.player1.score else game.player2.score

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        Column(
            modifier = modifier
                .padding(dimensionResource(R.dimen.padding_medium))
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Game Finished",
                fontSize = 24.sp,
                fontWeight = SemiBold,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small))
            )
            Text(
                text = if(game.winnerId == userId) "You have won!!" else "You have lost :(",
                fontSize = 18.sp,
                fontWeight = SemiBold,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_medium))
            )
            Text(
                text = "Initial Score: $initialScore",
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_medium))
            )
            Text(
                text = "Final Score: $userScore",
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_medium))
            )
            Row {
                Button(
                    onClick = onClickLeave,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_small))
                        .sizeIn(minWidth = 150.dp)
                ) {
                    Text(text = "Leave Game")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GameFinishedSectionPreview(){
    SeaBattleTheme {
        GameFinishedSection(
            modifier = Modifier,
            game = gameSample1,
            userId = "user1",
            userScore = 1000,
        )
    }
}