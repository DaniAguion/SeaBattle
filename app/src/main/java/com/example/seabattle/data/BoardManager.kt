package com.example.seabattle.data

import com.example.seabattle.domain.model.ShipDirection
import com.example.seabattle.domain.model.GameBoard
import com.example.seabattle.domain.model.Position
import com.example.seabattle.domain.model.Ship


class BoardManager {
    private val gameBoard: GameBoard = GameBoard()

    init {
        val ships = listOf(
            Ship(size = 5, setPosition(5)),
            Ship(size = 4, setPosition(4)),
            Ship(size = 3, setPosition(3)),
            Ship(size = 3, setPosition(3)),
            Ship(size = 2, setPosition(2)),
        )
    }

    fun setPosition(shipSize: Int) : Position {
        val position = randomPosition(shipSize)
        for (i in 0 until shipSize)
            when (position.shipDirection) {
                ShipDirection.VERTICAL -> gameBoard.cells[position.x][position.y + i] = 1
                ShipDirection.HORIZONTAL -> gameBoard.cells[position.x + i][position.y] = 1
            }
        return position
    }

    fun randomPosition(shipSize: Int): Position {
        while (true) {
            val shipDirection = ShipDirection.entries.random()
            var x: Int
            var y: Int

            when (shipDirection){
                ShipDirection.VERTICAL -> {
                    x = (0 until gameBoard.cells.size).random()
                    y = (0 until gameBoard.cells[0].size - shipSize).random()

                    // Limits the position to ensure ships separation
                    val x0 = if (x > 0) (x-1) else x
                    val y0 = if (y > 0) (y-1) else y
                    val x1 = if (x < gameBoard.cells.size - 1) (x + 1) else x
                    val y1 = if (y + shipSize < gameBoard.cells[0].size - 1) (y + shipSize + 1) else (y + shipSize)

                    if (checkPosition(gameBoard, x0, x1, y0, y1)) {
                        return Position(x, y, shipDirection)
                    }
                }
                ShipDirection.HORIZONTAL -> {
                    x = (0 until gameBoard.cells.size - shipSize).random()
                    y = (0 until gameBoard.cells[0].size).random()

                    // Limits the position to ensure ships separation
                    val x0 = if (x > 0) (x-1) else x
                    val y0 = if (y > 0) (y-1) else y
                    val x1 = if (x + shipSize < gameBoard.cells.size - 1) (x + shipSize + 1) else (x + shipSize)
                    val y1 = if (y < gameBoard.cells[0].size - 1) (y + 1) else y

                    if (checkPosition(gameBoard, x0, x1, y0, y1)) {
                        return Position(x, y, shipDirection)
                    }
                }
            }
        }
    }

    fun checkPosition(
        gameBoard: GameBoard,
        x0: Int,
        x1: Int,
        y0: Int,
        y1: Int
    ): Boolean {

        for (x in x0..x1) {
            for (y in y0 .. y1) {
                if (gameBoard.cells[x][y] != 0) {
                    return false
                }
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

    fun getGameBoard(): GameBoard {
        return gameBoard
    }
}