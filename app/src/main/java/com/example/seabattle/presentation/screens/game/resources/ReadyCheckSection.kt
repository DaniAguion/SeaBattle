package com.example.seabattle.presentation.screens.game.resources

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.seabattle.R
import com.example.seabattle.data.local.sampleGame
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.presentation.theme.SeaBattleTheme


@Composable
fun ReadyCheckSection(
    modifier: Modifier = Modifier,
    game: Game,
    onClickReady: () -> Unit = {},
    enableReadyButton : Boolean = true,
    onClickLeave: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_small))
    ) {
        Column(
            modifier = modifier
                .padding(dimensionResource(R.dimen.padding_small))
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.ready_check_title),
                fontSize = 20.sp,
                fontWeight = SemiBold,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_xsmall))
            )
            Text(
                text = stringResource(R.string.ready_check_desc),
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_xsmall))
            )
            Text(
                text = if (game.player1Ready) {
                    stringResource(R.string.player_ready, game.player1.displayName)
                } else {
                    stringResource(R.string.player_not_ready, game.player1.displayName)
                },
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_xsmall))
            )
            Text(
                text = if (game.player2Ready) {
                    stringResource(R.string.player_ready, game.player2.displayName)
                } else {
                    stringResource(R.string.player_not_ready, game.player2.displayName)
                },
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_xsmall))
            )
            Row {
                Button(
                    onClick = onClickLeave,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_xsmall))
                ) {
                    Text(stringResource(R.string.leave_game_button))
                }
                Button(
                    onClick = onClickReady,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    enabled = enableReadyButton,
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_xsmall))
                ) {
                    Text(stringResource(R.string.ready_button))
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ReadyCheckSectionPreview(){
    SeaBattleTheme {
        ReadyCheckSection(
            game = sampleGame
        )
    }
}