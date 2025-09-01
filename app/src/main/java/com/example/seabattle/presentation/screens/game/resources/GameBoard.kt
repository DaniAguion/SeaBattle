package com.example.seabattle.presentation.screens.game.resources

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getColor
import com.example.seabattle.R
import com.example.seabattle.data.local.sampleGame
import com.example.seabattle.domain.entity.CellState
import com.example.seabattle.presentation.theme.SeaBattleTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.random.Random
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription


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
    val systemBarInsets = WindowInsets.systemBars.asPaddingValues()
    val screenWidthDp = with(density) { windowSize.width.toDp() }
    val screenHeightDp = with(density) { windowSize.height.toDp() }
    val padding = dimensionResource(R.dimen.padding_small)
    val cellPadding = dimensionResource(R.dimen.cell_padding)
    val availableWidth = screenWidthDp - (padding * 2) - cellPadding * 2 * boardSize

    // Calculate the available height by subtracting the padding, cell padding, and system bar insets
    // This ensures that the game board fits within the visible area of the screen
    val availableHeight = screenHeightDp - (padding* 2) - cellPadding * 2 * boardSize - systemBarInsets.calculateTopPadding() - systemBarInsets.calculateBottomPadding()
    // Calculate the cell size based on the available width and height
    val calculatedCellSize = (minOf(availableWidth.value, availableHeight.value) / boardSize).dp

    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(
                    vertical = dimensionResource(R.dimen.padding_small),
                    horizontal = dimensionResource(R.dimen.padding_small)
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (i in 0 until gameBoard.size) {
                Row  {
                    for (j in 0 until (gameBoard[i.toString()]?.size ?: 0)) {
                        Cell(
                            row = i,
                            col = j,
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
    row: Int,
    col: Int,
    cellValue: Int,
    cellUnhidden : Boolean,
    onClickCell: () -> Unit,
    clickEnabled: Boolean,
    cellSize: Dp
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    val positionDescription = context.getString(R.string.cell_position, row + 1, col + 1)
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


    // Determine the content shape based on the cell value and whether the user is allowed to see the cell
    // It is better to avoid to show the ship shape to the opponent to make the game more interesting
    val contentShape =
        when(cellValue){
            CellState.SHIP_TOP.value,
            CellState.SUNK_TOP.value -> CellShape.SHIP_TOP
            CellState.HIT_TOP.value -> if (cellUnhidden) { CellShape.SHIP_TOP } else { CellShape.SQUARE }

            CellState.SHIP_BOTTOM.value,
            CellState.SUNK_BOTTOM.value -> CellShape.SHIP_BOTTOM
            CellState.HIT_BOTTOM.value -> if (cellUnhidden) { CellShape.SHIP_BOTTOM } else { CellShape.SQUARE }

            CellState.SHIP_START.value,
            CellState.SUNK_START.value -> CellShape.SHIP_START
            CellState.HIT_START.value -> if (cellUnhidden) { CellShape.SHIP_START } else { CellShape.SQUARE }

            CellState.SHIP_END.value,
            CellState.SUNK_END.value -> CellShape.SHIP_END
            CellState.HIT_END.value -> if (cellUnhidden) { CellShape.SHIP_END } else { CellShape.SQUARE }
        else -> CellShape.SQUARE
    }


    // Determine the final mask cell style that will be used to hide the cell content
    val finalMaskCellStyle: CellStyle =
        if (cellUnhidden) {
            CellStyle.None
        } else {
            when(cellValue){
                CellState.HIDDEN_WATER.value,
                CellState.SHIP.value,
                CellState.SHIP_TOP.value,
                CellState.SHIP_BOTTOM.value,
                CellState.SHIP_START.value,
                CellState.SHIP_END.value -> CellStyle.Unknown

                else -> CellStyle.None
            }
        }


    // Determine the final target style it will be used to show the target circle and the damage on the ship
    val finalTargetStyle: TargetStyle =
        when(cellValue){
            CellState.HIT.value -> TargetStyle.Hit
            CellState.HIT_TOP.value -> TargetStyle.Hit
            CellState.HIT_BOTTOM.value -> TargetStyle.Hit
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
    val (currentAnimateMaskCellStyle, setCurrentAnimateMaskCellStyle) = remember { mutableStateOf(finalMaskCellStyle) }
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
            setCurrentAnimateMaskCellStyle(CellStyle.None)
            setCurrentAnimateTargetStyle(finalTargetStyle)
            setCurrentCellValue(finalCellValue)
        }
    }

    // Animate the cell change
    val animatedCellColor by animateColorAsState(
        targetValue = colorResource(id = currentAnimateCellStyle.backgroundColor),
        animationSpec = tween(durationMillis = animationDuration),
        label = "cellContentColorAnimation"
    )

    // Animate the unmask of the cell
    val animatedMaskCellColor by animateColorAsState(
        targetValue = colorResource(id = currentAnimateMaskCellStyle.backgroundColor),
        animationSpec = tween(durationMillis = animationDuration),
        label = "cellMaskColorAnimation"
    )

    // Animate the target change of color
    val animatedTargetColor by animateColorAsState(
        targetValue = Color(getColor(context, currentAnimateTargetStyle.targetColor)),
        animationSpec = tween(durationMillis = animationDuration),
        label = "targetColorAnimation"
    )



    // Animate the target size
    // Animate only if the target style is Target, otherwise make the change instantly
    // The idea is to animate the size when changes from None to Target style
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

    val actionLabel = R.string.action_reveal_cell


    // Draw the cell
    Surface(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.cell_padding))
            .size(cellSize)
            .clickable(
                enabled = cellClickable,
                onClick = onClickCell
            )
            .semantics(mergeDescendants = true) {
                contentDescription = positionDescription
                stateDescription = context.getString(finalCellStyle.cellDescription)
                role = Role.Button
                if (!cellClickable) disabled()
                onClick(label = context.getString(actionLabel)) { true }
            },
        shape = RectangleShape,
        color = Color.Transparent,
        contentColor = LocalContentColor.current,
    ) {
        Canvas(
            modifier = Modifier.size(cellSize)
        ) {
            val width = size.width
            val height = size.height

            // Path for the cell shape based on the cell value
            val cellPath = if (contentShape == CellShape.SHIP_TOP) {
                Path().apply {
                    moveTo(width / 2f, 0f)
                    lineTo(width, height/1.5f)
                    lineTo(width, height)
                    lineTo(0f, height)
                    lineTo(0f, height/1.5f)
                    close()
                }
            } else if (contentShape == CellShape.SHIP_BOTTOM) {
                Path().apply {
                    moveTo(width / 2f, height)
                    lineTo(width, height/1.5f)
                    lineTo(width, 0f)
                    lineTo(0f, 0f)
                    lineTo(0f, height/1.5f)
                    close()
                }
            } else if (contentShape == CellShape.SHIP_START) {
                Path().apply {
                    moveTo(0f, height/ 2f)
                    lineTo(width/1.5f, height)
                    lineTo(width, height)
                    lineTo(width, 0f)
                    lineTo(width/1.5f, 0f)
                    close()
                }
            } else if (contentShape == CellShape.SHIP_END) {
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


            // Path for the target circle needed to contain the fire particles
            val targetCirclePath = Path().apply {
                val left = center.x - animatedTargetSize.value
                val top = center.y - animatedTargetSize.value
                val right = center.x + animatedTargetSize.value
                val bottom = center.y + animatedTargetSize.value
                addOval(Rect(left, top, right, bottom))
            }


            // Draw the water background
            drawRect(
                color = waterColor,
                size = size,
            )


            // Draw the contain of the cell
            clipPath(cellPath) {
                drawRect(
                    color = animatedCellColor,
                    size = size,
                )
            }


            // Draw the mask to hide the cell content
            drawRect(
                color = animatedMaskCellColor,
                size = size,
            )


            // Draw the target circle
            drawCircle(
                color = animatedTargetColor,
                radius = animatedTargetSize.value,
                center = center,
            )

            // Draw the fire particles
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
            gameBoard = sampleGame.boardForPlayer1,
            cellsUnhidden = true,
            onClickCell = { row, col -> },
            clickEnabled = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
