package com.example.seabattle.presentation.resources

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import com.example.seabattle.R
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.dimensionResource
import androidx.core.content.ContextCompat.getColor
import com.example.seabattle.domain.model.GameBoard


@Composable
fun Board(
    gameBoard: GameBoard,
    onCellClick: (row: Int, col: Int) -> Unit
) {
    Column(
        modifier = Modifier
    ) {
        for (i in 0 until gameBoard.cells.size) {
            Row {
                for (j in 0 until gameBoard.cells[i].size) {
                    Cell(
                        cellValue = gameBoard.cells[i][j],
                        onCellClick = {
                            onCellClick(i, j)
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun Cell(
    cellValue: Int,
    onCellClick: () -> Unit
) {
    val context = LocalContext.current
    val cellSize = dimensionResource(R.dimen.cell_size)

    Log.d("Cell", "Composing Cell con cellValue = $cellValue")

    val cellStyle: CellStyle =
        when(cellValue){
            0, 1 -> CellStyle.Target
            2 -> CellStyle.Water
            3 -> CellStyle.Hit
            4 -> CellStyle.Ship
            else -> CellStyle.Water
        }

    val cellClickable = cellStyle.clickable
    val cellColor : Color = colorResource(id = cellStyle.backgroundColor)
    val targetColorId = cellStyle.targetColor
    val targetSize = dimensionResource(cellStyle.targetSize)

    Surface(
        modifier = Modifier
            .size(cellSize)
            .clickable(
                enabled = cellClickable,
                onClick = {
                    onCellClick()
                }
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


@Preview (showBackground = true)
@Composable
fun BattlePlanPreview(){
    Board(
        gameBoard = GameBoard(),
        onCellClick = { _, _ ->  } // Dummy click handler for preview
    )
}