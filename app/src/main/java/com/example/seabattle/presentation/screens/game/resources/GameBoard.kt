package com.example.seabattle.presentation.screens.game.resources

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.getColor
import com.example.seabattle.R


@Composable
fun GameBoard(
    gameBoard: MutableMap<String, MutableMap<String, Int>>,
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
            for (i in 0 until gameBoard.size) {
                Row {
                    for (j in 0 until (gameBoard[i.toString()]?.size ?: 0)) {
                        Cell(
                            cellValue = gameBoard[i.toString()]?.get(j.toString()) ?: 0,
                            onClickCell = { onClickCell(i, j) },
                            clickEnabled = clickEnabled
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun Cell(
    cellValue: Int,
    onClickCell: () -> Unit,
    clickEnabled: Boolean
) {
    val context = LocalContext.current
    val cellSize = dimensionResource(R.dimen.cell_size)

    val cellStyle: CellStyle =
        when(cellValue){
            0, 1 -> CellStyle.Target
            2 -> CellStyle.Water
            3 -> CellStyle.Hit
            4 -> CellStyle.Ship
            else -> CellStyle.Water
        }

    val cellClickable = if (clickEnabled) cellStyle.clickable else false
    val cellColor : Color = colorResource(id = cellStyle.backgroundColor)
    val targetColorId = cellStyle.targetColor
    val targetSize = dimensionResource(cellStyle.targetSize)

    Surface(
        modifier = Modifier
            .size(cellSize)
            .clickable(
                enabled = cellClickable,
                onClick = onClickCell
            ),
        shape = RectangleShape,
        color = Color.Transparent,
        contentColor = LocalContentColor.current,
    ) {
        Canvas(
            modifier = Modifier.size(cellSize)
        ) {
            drawRect(
                color = cellColor,
                size = size,
            )

            drawCircle(
                color = Color(getColor(context, targetColorId)),
                radius = targetSize.value,
                center = center,
            )
        }
        Text(
            text = cellValue.toString(),
            modifier = Modifier
                .size(cellSize)
        )
    }
}



@Preview(showBackground = true)
@Composable
fun PlayingSectionPreview(){
    GameBoard(
        gameBoard = mutableMapOf(
            "0" to mutableMapOf("0" to 1, "1" to 1, "2" to 1, "3" to 1, "4" to 0, "5" to 0, "6" to 0, "7" to 0, "8" to 0, "9" to 0),
            "1" to mutableMapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 0, "7" to 0, "8" to 0, "9" to 0),
            "2" to mutableMapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 0, "7" to 0, "8" to 0, "9" to 0),
            "3" to mutableMapOf("0" to 0, "1" to 1, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 1, "7" to 0, "8" to 0, "9" to 0),
            "4" to mutableMapOf("0" to 0, "1" to 1, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 1, "7" to 0, "8" to 0, "9" to 0),
            "5" to mutableMapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 1, "7" to 0, "8" to 0, "9" to 1),
            "6" to mutableMapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 1, "7" to 0, "8" to 0, "9" to 1),
            "7" to mutableMapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 1, "7" to 0, "8" to 0, "9" to 1),
            "8" to mutableMapOf("0" to 1, "1" to 1, "2" to 1, "3" to 0, "4" to 0, "5" to 0, "6" to 0, "7" to 0, "8" to 0, "9" to 0),
            "9" to mutableMapOf("0" to 0, "1" to 0, "2" to 0, "3" to 0, "4" to 0, "5" to 0, "6" to 0, "7" to 0, "8" to 0, "9" to 0)
        ),
        onClickCell = { row, col -> },
        clickEnabled = true
    )
}
