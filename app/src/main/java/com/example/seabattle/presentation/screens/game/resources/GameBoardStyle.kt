package com.example.seabattle.presentation.screens.game.resources

import com.example.seabattle.R

sealed class CellStyle(
    val clickable: Boolean = true,
    val backgroundColor: Int,
    val cellDescription: Int
    ) {
    object Unknown : CellStyle(
        clickable = true,
        backgroundColor = R.color.cloud_color,
        cellDescription = R.string.unknown_cell
    )
    object Hit : CellStyle(
        clickable = false,
        backgroundColor = R.color.ship_color,
        cellDescription = R.string.hit_cell
    )
    object Water : CellStyle(
        clickable = false,
        backgroundColor = R.color.water_color,
        cellDescription = R.string.water_cell
    )
    object Ship : CellStyle(
        clickable = false,
        backgroundColor = R.color.ship_color,
        cellDescription = R.string.ship_cell
    )
    object Sunk : CellStyle(
        clickable = false,
        backgroundColor = R.color.sunk_ship_color,
        cellDescription = R.string.sunk_cell
    )
    object None : CellStyle(
        clickable = false,
        backgroundColor = R.color.none_color,
        cellDescription = R.string.none_cell
    )
}

enum class CellShape {
    SQUARE,
    SHIP_TOP,
    SHIP_BOTTOM,
    SHIP_START,
    SHIP_END,
}

sealed class TargetStyle(
    val targetColor: Int,
    val targetSize: Int
) {
    object Target : TargetStyle(
        targetColor = R.color.target_color,
        targetSize = R.dimen.target_size,
    )
    object Hit : TargetStyle(
        targetColor = R.color.hit_color,
        targetSize = R.dimen.hit_size,
    )
    object None : TargetStyle(
        targetColor = R.color.none_color,
        targetSize = R.dimen.none_size,
    )
}