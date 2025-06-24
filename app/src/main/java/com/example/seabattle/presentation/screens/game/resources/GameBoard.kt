package com.example.seabattle.presentation.screens.game.resources

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.seabattle.data.local.gameSample1
import kotlinx.coroutines.delay


@Composable
fun GameBoard(
    gameBoard: Map<String, Map<String, Int>>,
    cellsUnhidden: Boolean = false,
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
                            cellUnhidden = cellsUnhidden,
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
    cellUnhidden : Boolean,
    onClickCell: () -> Unit,
    clickEnabled: Boolean
) {
    val context = LocalContext.current
    val cellSize = dimensionResource(R.dimen.cell_size)

    val finalCellValue = cellValue

    val finalCellStyle: CellStyle =
        if (cellUnhidden) {
            when(cellValue){
                0, 2 -> CellStyle.Water
                1, 4 -> CellStyle.Ship
                3 -> CellStyle.Hit
                5 -> CellStyle.Sunk
                else -> CellStyle.Water
            }
        } else {
            when(cellValue){
                0, 1 -> CellStyle.Unknown
                2 -> CellStyle.Water
                3 -> CellStyle.Hit
                4 -> CellStyle.Ship
                5 -> CellStyle.Sunk
                else -> CellStyle.Water
            }
        }

    val finalTargetStyle: TargetStyle =
        when(cellValue){
            3 -> TargetStyle.Hit
            else -> TargetStyle.None
        }


    val cellClickable = if (clickEnabled) finalCellStyle.clickable else false

    val (currentCellValue, setCurrentCellValue) = remember { mutableIntStateOf(finalCellValue) }
    val (currentAnimateCellStyle, setCurrentAnimateCellStyle) = remember { mutableStateOf(finalCellStyle) }
    val (currentAnimateTargetStyle, setCurrentAnimateTargetStyle) = remember { mutableStateOf(finalTargetStyle) }
    val animationDuration = 800
    val animationDelay : Long = animationDuration.toLong()

    // Introduce the target style animation and animate the change of the cell style
    LaunchedEffect(finalCellValue) {
        if (currentCellValue != finalCellValue) {
            //If the ship wont be sunk, animate the target style to Target
            if (finalCellStyle != CellStyle.Sunk) {
                setCurrentAnimateTargetStyle(TargetStyle.Target)
                delay(timeMillis = animationDelay)
                setCurrentAnimateCellStyle(finalCellStyle)
            }
            else {
                // If the ship will be sunk, animate the target style if it was not Hit before
                // if the cell was Hit before, just wait
                if (currentAnimateCellStyle != CellStyle.Hit) {
                    setCurrentAnimateTargetStyle(TargetStyle.Target)
                    delay(timeMillis = animationDelay)
                    setCurrentAnimateTargetStyle(TargetStyle.Hit)
                    delay(timeMillis = animationDelay)
                } else {
                    delay(timeMillis = animationDelay*2)
                }
                setCurrentAnimateCellStyle(finalCellStyle)
            }
            setCurrentAnimateTargetStyle(finalTargetStyle)
        }
        setCurrentCellValue(finalCellValue)
    }

    val animatedCellColor by animateColorAsState(
        targetValue = colorResource(id = currentAnimateCellStyle.backgroundColor),
        animationSpec = tween(durationMillis = animationDuration),
        label = "cellBackgroundColorAnimation"
    )

    val animatedTargetColor by animateColorAsState(
        targetValue = Color(getColor(context, currentAnimateTargetStyle.targetColor)),
        animationSpec = tween(durationMillis = animationDuration),
        label = "targetColorAnimation"
    )

    val animatedTargetSize by animateDpAsState(
        targetValue = dimensionResource(currentAnimateTargetStyle.targetSize),
        animationSpec =
            if(currentAnimateTargetStyle == TargetStyle.Target) {
                tween(durationMillis = animationDuration)
            }  else {
                tween(durationMillis = 0)
            }
        ,
        label = "targetSizeAnimation"
    )

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
                color = animatedCellColor,
                size = size,
            )

            drawCircle(
                color = animatedTargetColor,
                radius = animatedTargetSize.value,
                center = center,
            )

            drawCircle(
                color = animatedTargetColor,
                radius = animatedTargetSize.value,
                center = center,
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PlayingSectionPreview(){
    GameBoard(
        gameBoard = gameSample1.boardForPlayer1,
        cellsUnhidden = true,
        onClickCell = { row, col -> },
        clickEnabled = true
    )
}
