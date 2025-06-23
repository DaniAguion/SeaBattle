package com.example.seabattle.presentation.screens.game.resources

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.sp
import com.example.seabattle.R
import com.example.seabattle.domain.entity.Game
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
    // Delayed current player variable used to delay the switch of game board
    var delayedCurrentPlayer by remember { mutableStateOf(game.currentPlayer) }

    LaunchedEffect(key1 = game.currentPlayer) {
        delay(2000)
        delayedCurrentPlayer = game.currentPlayer
    }


    if (delayedCurrentPlayer == userId) {
        Text(
            text = "It's your turn!",
            fontSize = 20.sp,
            fontWeight = SemiBold,
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_medium))
        )
    } else {
        Text(
            text = "It's your opponent's turn!",
            fontSize = 20.sp,
            fontWeight = SemiBold,
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_medium))
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
                clickEnabled = enableClickCell("player1")
            )
        } else {
            GameBoard(
                gameBoard = game.boardForPlayer2,
                cellsUnhidden = enableSeeShips("player1"),
                onClickCell = onClickCell,
                clickEnabled = enableClickCell("player2")
            )
        }
    }
}