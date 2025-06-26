package com.example.seabattle.presentation.screens.game.resources

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.seabattle.R
import com.example.seabattle.data.local.gameSample1
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.presentation.theme.SeaBattleTheme
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit


@Composable
fun WaitGameSection(
    modifier: Modifier = Modifier,
    game: Game,
    onClickLeave: () -> Unit = {}
) {
    var waitingTime by remember(game.createdAt) {
        val initialMillis: Long = game.createdAt?.let {
            System.currentTimeMillis() - it.time
        } ?: 0L
        val initialSeconds = (initialMillis / 1000L).coerceAtLeast(0L).toInt()
        mutableStateOf(initialSeconds)
    }

    LaunchedEffect(waitingTime) {
        while (true) {
            val waitingTimeMS: Long = game.createdAt?.let {
                System.currentTimeMillis() - it.time
            } ?: 0L
            waitingTime = (waitingTimeMS / 1000L).coerceAtLeast(0L).toInt()
            delay(1000L)
        }
    }


    fun formatTime(seconds: Int): String {
        val minutes = TimeUnit.SECONDS.toMinutes(seconds.toLong())
        val remainingSeconds = seconds - TimeUnit.MINUTES.toSeconds(minutes)
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }


    Column(
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_medium))
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        // Header
        Text(
            text = stringResource(R.string.waiting_player),
            fontSize = 20.sp,
            fontWeight = SemiBold,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        )
        Text(
            text = stringResource(id = R.string.game_name, game.gameName),
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        )
        if (game.gameState == "WAITING_FOR_PLAYER") {
            Text(
                text = stringResource(id = R.string.waiting_time, formatTime(waitingTime)),
                fontSize = 18.sp,
                color = if (waitingTime >= 300) Color.Red else Color.Black,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
            )
            Button(
                onClick = onClickLeave,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            ) {
                Text(text = stringResource(R.string.leave_game_button))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun WaitGameSectionPreview(){
    SeaBattleTheme {
        WaitGameSection(
            game = gameSample1,
            onClickLeave = {}
        )
    }
}