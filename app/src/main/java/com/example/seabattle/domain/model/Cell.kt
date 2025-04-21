package com.example.seabattle.domain.model

import com.example.seabattle.presentation.resources.CellStyle

data class Cell(
    val cellValue: Int,
    val cellStyle: CellStyle =
        when (cellValue) {
            0 -> CellStyle.Target
            1 -> CellStyle.Target
            2 -> CellStyle.Water
            3 -> CellStyle.Hit
            4 -> CellStyle.Ship
            else -> CellStyle.Water
        }
)
