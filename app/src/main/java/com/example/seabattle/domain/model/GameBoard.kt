package com.example.seabattle.domain.model

data class GameBoard (
    val cells: List<List<Cell>> = List(10) { List(10) { Cell(cellValue = 0) } }
)