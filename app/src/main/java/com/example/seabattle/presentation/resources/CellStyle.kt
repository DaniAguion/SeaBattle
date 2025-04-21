package com.example.seabattle.presentation.resources

import com.example.seabattle.R

sealed class CellStyle(
    val clickable: Boolean = true,
    val targetColor: Int,
    val targetSize: Int,
    val backgroundColor: Int,
    ) {
    object Target : CellStyle(
        clickable = true,
        targetColor = R.color.target_color,
        targetSize = R.dimen.target_size,
        backgroundColor = R.color.cloud_color
    )
    object Ship : CellStyle(
        clickable = false,
        targetColor = R.color.none_color,
        targetSize = R.dimen.none_size,
        backgroundColor = R.color.ship_color
    )
    object Hit : CellStyle(
        clickable = false,
        targetColor = R.color.hit_color,
        targetSize = R.dimen.hit_size,
        backgroundColor = R.color.ship_color
    )
    object Water : CellStyle(
        clickable = false,
        targetColor = R.color.none_color,
        targetSize = R.dimen.none_size,
        backgroundColor = R.color.water_color
    )
}