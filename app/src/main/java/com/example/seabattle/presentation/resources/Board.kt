package com.example.seabattle.presentation.resources

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import com.example.seabattle.R
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.dimensionResource
import androidx.core.content.ContextCompat.getColor
import com.example.seabattle.domain.model.GameBoard


@Composable
fun Board(
    gameBoard: GameBoard,
    onCellClick: (CellStyle) -> Unit = {}
) {
    Column(
        modifier = Modifier
    ) {
        for (i in 0 until gameBoard.cells.size) {
            Row {
                for (j in 0 until gameBoard.cells[i].size) {
                    Cell(
                        cell = gameBoard.cells[i][j].cellStyle,
                        onCellClick = onCellClick
                    )
                }
            }
        }
    }
}

@Composable
fun Cell(
    cell: CellStyle,
    onCellClick: (CellStyle) -> Unit = {}
) {
    val context = LocalContext.current
    val cellSize = dimensionResource(R.dimen.cell_size)

    var cellColor : Color = colorResource(id = cell.backgroundColor)
    val targetColorId = cell.targetColor
    val targetSize = dimensionResource(cell.targetSize)

    Surface(
        modifier = Modifier
            .size(cellSize)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    onCellClick(cell)
                })
            },
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
    }

}


@Preview (showBackground = true)
@Composable
fun BattlePlanPreview(){
    Board(
        gameBoard = GameBoard(),
        onCellClick = {}
    )
}