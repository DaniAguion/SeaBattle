package com.example.seabattle.presentation.screens.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.seabattle.R
import com.example.seabattle.presentation.screens.game.board.Board


@Composable
fun PlayingSection(
    gameBoard: Map<String, Map<String, Int>>,
    onClickCell: (row: Int, col: Int) -> Unit,
    clickEnabled: Boolean
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
            Board(
                gameBoard = gameBoard,
                onCellClick = onClickCell,
                clickEnabled = clickEnabled
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PlayingSectionPreview(){
    PlayingSection(
        gameBoard = mapOf(
            "0" to mapOf("0" to 1, "1" to 1, "2" to 1, "3" to 1, "4" to 0, "5" to 0, "6" to 0, "7" to 0, "8" to 0, "9" to 0),
            "1" to mapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 0, "7" to 0, "8" to 0, "9" to 0),
            "2" to mapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 0, "7" to 0, "8" to 0, "9" to 0),
            "3" to mapOf("0" to 0, "1" to 1, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 1, "7" to 0, "8" to 0, "9" to 0),
            "4" to mapOf("0" to 0, "1" to 1, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 1, "7" to 0, "8" to 0, "9" to 0),
            "5" to mapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 1, "7" to 0, "8" to 0, "9" to 1),
            "6" to mapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 1, "7" to 0, "8" to 0, "9" to 1),
            "7" to mapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 1, "7" to 0, "8" to 0, "9" to 1),
            "8" to mapOf("0" to 1, "1" to 1, "2" to 1, "3" to 0, "4" to 0, "5" to 0, "6" to 0, "7" to 0, "8" to 0, "9" to 0),
            "9" to mapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 0, "7" to 0, "8" to 0, "9" to 0)
        ),
        onClickCell = { row, col -> },
        clickEnabled = true
    )
}
