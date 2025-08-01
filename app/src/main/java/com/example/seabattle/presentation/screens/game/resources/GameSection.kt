package com.example.seabattle.presentation.screens.game.resources

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.tooling.preview.Preview
import com.example.seabattle.R
import com.example.seabattle.data.local.sampleGame
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.presentation.theme.SeaBattleTheme
import kotlinx.coroutines.delay


@Composable
fun GameSection(
    modifier: Modifier = Modifier,
    game: Game,
    userId: String,
    onClickCell: (row: Int, col: Int) -> Unit = { _, _ -> },
    enableClickCell: (gameBoardOwner: String) -> Boolean = { true },
    enableSeeShips: (watcher: String) -> Boolean = { false }
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    // Delayed current player variable used to delay the switch of game board
    var delayedCurrentPlayer by remember { mutableStateOf(game.currentPlayer) }

    LaunchedEffect(key1 = game.currentPlayer) {
        delay(2000)
        delayedCurrentPlayer = game.currentPlayer
    }

    val gameLayout = @Composable { content: @Composable () -> Unit ->
        if (isLandscape) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                content()
            }
        } else {
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                content()
            }
        }
    }

    gameLayout {
        GameSectionContent(
            modifier = Modifier,
            game = game,
            userId = userId,
            delayedCurrentPlayer = delayedCurrentPlayer,
            onClickCell = onClickCell,
            enableClickCell = enableClickCell,
            enableSeeShips = enableSeeShips
        )
    }

}


@Composable
fun GameSectionContent(
    modifier: Modifier = Modifier,
    game: Game,
    userId: String,
    delayedCurrentPlayer: String,
    onClickCell: (row: Int, col: Int) -> Unit = { _, _ -> },
    enableClickCell: (gameBoardOwner: String) -> Boolean = { true },
    enableSeeShips: (watcher: String) -> Boolean = { false }
) {
    if (delayedCurrentPlayer == userId) {
        Text(
            text = stringResource(R.string.your_turn),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = SemiBold,
            modifier = Modifier
                .padding(bottom = dimensionResource(R.dimen.padding_medium))
                .padding(horizontal = dimensionResource(R.dimen.padding_medium))
        )
    } else {
        Text(
            text = stringResource(R.string.oponnent_turn),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = SemiBold,
            modifier = Modifier
                .padding(bottom = dimensionResource(R.dimen.padding_medium))
                .padding(horizontal = dimensionResource(R.dimen.padding_medium))
        )
    }
    AnimatedContent(
        targetState = delayedCurrentPlayer,
        transitionSpec = {
            if (targetState == game.player1.userId) {
                // If the board is for player 1, slide in from the left and slide out to the right
                slideInHorizontally(animationSpec = tween(700, easing = EaseOutQuart)) { fullWidth -> -fullWidth } + fadeIn(tween(500)) togetherWith
                        slideOutHorizontally(animationSpec = tween(700, easing = EaseOutQuart)) { fullWidth -> +fullWidth } + fadeOut(tween(500))
            } else {
                // If the board is for player 2, slide in from the right and slide out to the left
                slideInHorizontally(animationSpec = tween(700, easing = EaseOutQuart)) { fullWidth -> +fullWidth } + fadeIn(tween(500)) togetherWith
                        slideOutHorizontally(animationSpec = tween(700, easing = EaseOutQuart)) { fullWidth -> -fullWidth } + fadeOut(tween(500))
            }
        }, label = "GameBoardTransition"
    ) { delayedCurrentPlayer ->
        if (delayedCurrentPlayer == game.player1.userId) {
            GameBoard(
                gameBoard = game.boardForPlayer1,
                cellsUnhidden = enableSeeShips("player2"),
                onClickCell = onClickCell,
                clickEnabled = enableClickCell("player1"),
                modifier = modifier
            )
        } else {
            GameBoard(
                gameBoard = game.boardForPlayer2,
                cellsUnhidden = enableSeeShips("player1"),
                onClickCell = onClickCell,
                clickEnabled = enableClickCell("player2"),
                modifier = modifier
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GameSectionPreview(){
    SeaBattleTheme {
        GameSection(
            modifier = Modifier.fillMaxSize(),
            game = sampleGame,
            userId = sampleGame.player1.userId
        )
    }
}