package com.example.seabattle.data

import com.example.seabattle.domain.model.Cell
import com.example.seabattle.domain.model.GameBoard

class BoardManager {
    private val board: GameBoard = GameBoard(
        cells = List(10) { List(10) { Cell(cellValue = 0) } }
    )

    fun getBoard(): GameBoard {
        return board
    }
}