package com.example.seabattle.domain.model

import com.example.seabattle.presentation.resources.CellStyle

data class Cell(
    val cellValue: Int,
    val cellStyle: CellStyle =
        when (cellValue) {
            0 -> CellStyle.Target
            1 -> CellStyle.Water
            2 -> CellStyle.Hit
            3 -> CellStyle.Ship
            else -> CellStyle.Water
        }
)
