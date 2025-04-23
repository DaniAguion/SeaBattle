package com.example.seabattle.data

import com.example.seabattle.domain.model.ShipDirection
import com.example.seabattle.domain.model.GameBoard
import com.example.seabattle.domain.model.Position
import com.example.seabattle.domain.model.Ship


class BoardManager {
    private val gameBoard: GameBoard = GameBoard()
    val gameBoardSize: Int = gameBoard.cells.size
    val ships: List<Ship>

    init {
        ships = seedGameBoard()
    }


    fun getGameBoard(): GameBoard {
        return gameBoard
    }


    private fun seedGameBoard() : List<Ship> {
        val shipSizes = listOf(5, 4, 3, 3, 2)
        return shipSizes.map { size -> Ship(size, setPosition(size)) }
    }


    // Function to set the position of a ship on the game board
    private fun setPosition(shipSize: Int) : Position {
        val position = definePosition(shipSize)
        for (i in 0 until shipSize)
            when (position.shipDirection) {
                ShipDirection.VERTICAL -> gameBoard.cells[position.x][position.y + i] = 1
                ShipDirection.HORIZONTAL -> gameBoard.cells[position.x + i][position.y] = 1
            }
        return position
    }


    // Function to define a random position for a ship on the game board
    private fun definePosition(shipSize: Int): Position {
        while (true) {
            val shipDirection = ShipDirection.entries.random()
            var x0: Int
            var y0: Int
            var x1: Int
            var y1: Int

            when (shipDirection){
                ShipDirection.VERTICAL -> {
                    x0 = (0 until gameBoardSize).random()
                    y0 = (0 until gameBoardSize - shipSize).random()
                    x1 = x0
                    y1 = y0 + shipSize
                }
                ShipDirection.HORIZONTAL -> {
                    x0 = (0 until gameBoardSize - shipSize).random()
                    y0 = (0 until gameBoardSize).random()
                    x1 = x0 + shipSize
                    y1 = y0
                }
            }

            if (checkPosition(x0, x1, y0, y1)) {
                return Position(x0, y0, shipDirection)
            }
        }
    }


    // Function to check if a ship can be placed at the given position
    private fun checkPosition(x0: Int, x1: Int, y0: Int, y1: Int): Boolean {
        // Extend the area to ensure that the ship is not placed next to another ship
        val xMin = (x0 - 1).coerceAtLeast(0)
        val xMax = (x1 + 1).coerceAtMost(gameBoardSize - 1)
        val yMin = (y0 - 1).coerceAtLeast(0)
        val yMax = (y1 + 1).coerceAtMost(gameBoardSize - 1)

        for (x in xMin..xMax) {
            for (y in yMin .. yMax) {
                if (gameBoard.cells[x][y] != 0) return false
            }
        }
        return true
    }


    fun discoverCell(x: Int, y: Int) : GameBoard {
        when (gameBoard.cells[x][y]) {
            0 -> gameBoard.cells[x][y] = 2
            1 -> gameBoard.cells[x][y] = 3
            else -> gameBoard.cells[x][y] = gameBoard.cells[x][y]
        }
        return gameBoard
    }
}