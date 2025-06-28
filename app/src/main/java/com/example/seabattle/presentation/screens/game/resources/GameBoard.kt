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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getColor
import com.example.seabattle.R
import com.example.seabattle.data.local.gameSample1
import com.example.seabattle.domain.entity.CellState
import com.example.seabattle.presentation.theme.SeaBattleTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.random.Random


@Composable
fun GameBoard(
    gameBoard: Map<String, Map<String, Int>>,
    cellsUnhidden: Boolean,
    onClickCell: (row: Int, col: Int) -> Unit,
    clickEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val boardSize = gameBoard.size

    // Calculate screen width and height in dp
    val density = LocalDensity.current
    val windowSize = LocalWindowInfo.current.containerSize
    val screenWidthDp = with(density) { windowSize.width.toDp() }
    val screenHeightDp = with(density) { windowSize.height.toDp() }
    val padding = dimensionResource(R.dimen.padding_medium)
    val cellPadding = dimensionResource(R.dimen.cell_padding)
    val availableWidth = screenWidthDp - (padding * 2) - cellPadding * 2 * boardSize
    val availableHeight = screenHeightDp - (padding* 2) - cellPadding * 2 * boardSize

    // Calculate the cell size based on the available width and height
    val calculatedCellSize = (minOf(availableWidth.value, availableHeight.value) / boardSize).dp

    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = dimensionResource(R.dimen.padding_medium),
                    horizontal = dimensionResource(R.dimen.padding_medium)
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (i in 0 until gameBoard.size) {
                Row  {
                    for (j in 0 until (gameBoard[i.toString()]?.size ?: 0)) {
                        Cell(
                            cellValue = gameBoard[i.toString()]?.get(j.toString()) ?: 0,
                            cellUnhidden = cellsUnhidden,
                            onClickCell = { onClickCell(i, j) },
                            clickEnabled = clickEnabled,
                            cellSize = calculatedCellSize
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
    clickEnabled: Boolean,
    cellSize: Dp
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    val finalCellValue = cellValue

    val finalCellStyle: CellStyle =
        if (cellUnhidden) {
            when(cellValue){
                CellState.HIDDEN_WATER.value -> CellStyle.Water
                CellState.WATER.value -> CellStyle.Water

                CellState.SHIP.value,
                CellState.SHIP_TOP.value,
                CellState.SHIP_BOTTOM.value,
                CellState.SHIP_START.value,
                CellState.SHIP_END.value -> CellStyle.Ship

                CellState.HIT.value,
                CellState.HIT_TOP.value,
                CellState.HIT_BOTTOM.value,
                CellState.HIT_START.value,
                CellState.HIT_END.value -> CellStyle.Hit

                CellState.SUNK.value,
                CellState.SUNK_TOP.value,
                CellState.SUNK_BOTTOM.value,
                CellState.SUNK_START.value,
                CellState.SUNK_END.value -> CellStyle.Sunk
                else -> CellStyle.Water
            }
        } else {
            when(cellValue){
                CellState.HIDDEN_WATER.value -> CellStyle.Unknown
                CellState.WATER.value -> CellStyle.Water

                CellState.SHIP.value,
                CellState.SHIP_TOP.value,
                CellState.SHIP_BOTTOM.value,
                CellState.SHIP_START.value,
                CellState.SHIP_END.value -> CellStyle.Unknown

                CellState.HIT.value,
                CellState.HIT_TOP.value,
                CellState.HIT_BOTTOM.value,
                CellState.HIT_START.value,
                CellState.HIT_END.value -> CellStyle.Hit

                CellState.SUNK.value,
                CellState.SUNK_TOP.value,
                CellState.SUNK_BOTTOM.value,
                CellState.SUNK_START.value,
                CellState.SUNK_END.value -> CellStyle.Sunk
                else -> CellStyle.Water
            }
        }

    val finalTargetStyle: TargetStyle =
        when(cellValue){
            CellState.HIT.value -> TargetStyle.Hit
            CellState.HIT_START.value -> TargetStyle.Hit
            CellState.HIT_END.value -> TargetStyle.Hit
            else -> TargetStyle.None
        }

    val cellClickable = if (clickEnabled) finalCellStyle.clickable else false
    val waterColor = colorResource(id = CellStyle.Water.backgroundColor)


    //
    // Animation state management of the cell and target styles
    //

    val (currentCellValue, setCurrentCellValue) = remember { mutableIntStateOf(finalCellValue) }
    val (currentAnimateCellStyle, setCurrentAnimateCellStyle) = remember { mutableStateOf(finalCellStyle) }
    val (currentAnimateTargetStyle, setCurrentAnimateTargetStyle) = remember { mutableStateOf(finalTargetStyle) }
    val animationDuration = 1000
    val animationDelay : Long = animationDuration.toLong()

    // Introduce the target style animation and animate the change of the cell style
    LaunchedEffect(finalCellValue) {
        if (currentCellValue != finalCellValue) {
            // If the ship will be sunk, animate the target style only if it was not Hit before
            // If the ship wont be sunk, animate the target style to Target
            if (finalCellStyle == CellStyle.Sunk) {
                if (currentAnimateCellStyle != CellStyle.Hit) {
                    setCurrentAnimateTargetStyle(TargetStyle.Target)
                    delay(timeMillis = animationDelay)
                } else {
                    delay(timeMillis = animationDelay)
                }
            }
            else {
                setCurrentAnimateTargetStyle(TargetStyle.Target)
                delay(timeMillis = animationDelay)
            }
            setCurrentAnimateCellStyle(finalCellStyle)
            setCurrentAnimateTargetStyle(finalTargetStyle)
            setCurrentCellValue(finalCellValue)
        }
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




    //
    // Fire Animation
    //

    val showFire = remember { mutableStateOf(false) }
    val particles = remember { mutableStateListOf<FireParticle>() }
    var lastFrameTime by remember { mutableLongStateOf(System.nanoTime()) }


    LaunchedEffect(currentAnimateCellStyle) {
        if (currentAnimateCellStyle == CellStyle.Hit || currentAnimateCellStyle == CellStyle.Sunk) {
            showFire.value = true
            particles.clear()
            lastFrameTime = System.nanoTime()
        } else {
            showFire.value = false
            particles.clear()
        }
    }

    LaunchedEffect(showFire.value) {
        if (showFire.value) {
            val targetRadiusPx = with(density) { animatedTargetSize.toPx() }
            val particleMaxRadiusPx = with(density) { (cellSize / 2).toPx() }
            val particleMinSpeedPx = with(density) { (cellSize * 0.4f).toPx() }
            val particleMaxSpeedPx = with(density) { (cellSize).toPx() }

            while (isActive && showFire.value) {
                val currentTime = System.nanoTime()
                val deltaTime = (currentTime - lastFrameTime) / 1_000_000L
                lastFrameTime = currentTime


                val particlesToRemove = mutableListOf<FireParticle>()
                for (particle in particles) {
                    particle.update(deltaTime)
                    if (particle.isDead()) {
                        particlesToRemove.add(particle)
                    }
                }
                particles.removeAll(particlesToRemove)


                if (particles.size < 20 && Random.nextInt(100) < (1000L / 100)) {
                    val numToSpawn = Random.nextInt(1, 3)
                    repeat(numToSpawn) {
                        particles.add(
                            FireParticle.createRandom(
                                centerX = 0f,
                                startY = targetRadiusPx,
                                maxRadius = particleMaxRadiusPx,
                                minSpeed = particleMinSpeedPx,
                                maxSpeed = particleMaxSpeedPx
                            )
                        )
                    }
                }
                delay(10)
            }
        }
    }


    Surface(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.cell_padding))
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
                color = waterColor,
                size = size,
            )

            val width = size.width
            val height = size.height

            val cellPath = if (currentAnimateCellStyle == CellStyle.Unknown) {
                Path().apply {
                    val left = 0f
                    val top = 0f
                    val right = size.width
                    val bottom = size.height
                    addRect(Rect(left, top, right, bottom))
                }
            } else if ((cellValue == CellState.SHIP_TOP.value && cellUnhidden) ||
                cellValue == CellState.HIT_TOP.value ||
                cellValue == CellState.SUNK_TOP.value
            ) {
                Path().apply {
                    moveTo(width / 2f, 0f)
                    lineTo(width, height/1.5f)
                    lineTo(width, height)
                    lineTo(0f, height)
                    lineTo(0f, height/1.5f)
                    close()
                }
            } else if ((cellValue == CellState.SHIP_BOTTOM.value && cellUnhidden) ||
                cellValue == CellState.HIT_BOTTOM.value ||
                cellValue == CellState.SUNK_BOTTOM.value
            ) {
                Path().apply {
                    moveTo(width / 2f, height)
                    lineTo(width, height/1.5f)
                    lineTo(width, 0f)
                    lineTo(0f, 0f)
                    lineTo(0f, height/1.5f)
                    close()
                }
            } else if ((cellValue == CellState.SHIP_START.value && cellUnhidden) ||
                cellValue == CellState.HIT_START.value ||
                cellValue == CellState.SUNK_START.value) {
                Path().apply {
                    moveTo(0f, height/ 2f)
                    lineTo(width/1.5f, height)
                    lineTo(width, height)
                    lineTo(width, 0f)
                    lineTo(width/1.5f, 0f)
                    close()
                }
            } else if ((cellValue == CellState.SHIP_END.value && cellUnhidden) ||
                cellValue == CellState.HIT_END.value ||
                cellValue == CellState.SUNK_END.value) {
                Path().apply {
                    moveTo(width, height/ 2f)
                    lineTo(width/1.5f, 0f)
                    lineTo(0f, 0f)
                    lineTo(0f, height)
                    lineTo(width/1.5f, height)
                    close()
                }
            } else {
                Path().apply {
                    val left = 0f
                    val top = 0f
                    val right = size.width
                    val bottom = size.height
                    addRect(Rect(left, top, right, bottom))
                }
            }

            clipPath(cellPath) {
                drawRect(
                    color = animatedCellColor,
                    size = size,
                )
            }

            drawCircle(
                color = animatedTargetColor,
                radius = animatedTargetSize.value,
                center = center,
            )

            val targetCirclePath = Path().apply {
                val left = center.x - animatedTargetSize.value
                val top = center.y - animatedTargetSize.value
                val right = center.x + animatedTargetSize.value
                val bottom = center.y + animatedTargetSize.value
                addOval(Rect(left, top, right, bottom))
            }

            clipPath(targetCirclePath) {
                if (showFire.value) {
                    drawFireParticles(particles, center + Offset(0f, animatedTargetSize.value))
                }
            }
        }

    }
}



@Preview(showBackground = true)
@Composable
fun PlayingSectionPreview(){
    SeaBattleTheme {
        GameBoard(
            gameBoard = gameSample1.boardForPlayer1,
            cellsUnhidden = true,
            onClickCell = { row, col -> },
            clickEnabled = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
