package com.example.seabattle.presentation.screens.game.resources

import com.example.seabattle.R

sealed class CellStyle(
    val clickable: Boolean = true,
    val backgroundColor: Int,
    ) {
    object Unknown : CellStyle(
        clickable = true,
        backgroundColor = R.color.cloud_color
    )
    object Hit : CellStyle(
        clickable = false,
        backgroundColor = R.color.ship_color
    )
    object Water : CellStyle(
        clickable = false,
        backgroundColor = R.color.water_color
    )
    object Ship : CellStyle(
        clickable = false,
        backgroundColor = R.color.ship_color
    )
    object Sunk : CellStyle(
        clickable = false,
        backgroundColor = R.color.sunk_ship_color
    )
}


sealed class TargetStyle(
    val targetColor: Int,
    val targetSize: Int,
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