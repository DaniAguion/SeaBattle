package com.example.seabattle.presentation.resources

import com.example.seabattle.R
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview



@Composable
fun GameBoard(
    modifier : Modifier = Modifier,
) {
    var cellColor : Color = colorResource(id = R.color.light_blue)

    Column(
        modifier = Modifier
    ) {
        repeat(10) {
            Row {
                repeat(10) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .border(1.dp, Color.White   )
                            .background(cellColor)
                    )
                }
            }
        }
    }
}


@Preview (showBackground = true)
@Composable
fun BattlePlanPreview(){
    GameBoard()
}