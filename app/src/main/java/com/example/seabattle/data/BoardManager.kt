package com.example.seabattle.data

import com.example.seabattle.domain.model.GameBoard


class BoardManager {
    private val gameBoard: GameBoard = GameBoard()

    init {
        seedGameBoard()
    }

    fun seedGameBoard() {
        for (i in 0 until gameBoard.cells.size) {
            for (j in 0 until gameBoard.cells[i].size) {
                gameBoard.cells[i][j] = (0..1).random()
            }
        }
    }

    fun discoverCell(x: Int, y: Int) : GameBoard {
        when (gameBoard.cells[x][y]) {
            0 -> gameBoard.cells[x][y] = 2
            1 -> gameBoard.cells[x][y] = 3
            else -> gameBoard.cells[x][y] = gameBoard.cells[x][y]
        }
        return gameBoard
    }

    fun getGameBoard(): GameBoard {
        return gameBoard
    }
}