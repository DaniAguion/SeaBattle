package com.example.seabattle.data.local

import com.example.seabattle.domain.entity.Ship
import com.example.seabattle.domain.entity.ShipDirection
import com.example.seabattle.domain.entity.ShipPiece
import com.example.seabattle.domain.entity.ShipPosition
import com.example.seabattle.domain.repository.GameBoardRepository

// This repository is responsible for managing the game board and ship positions.
// It initializes a game board of size 10x10 and places ships of various sizes on it.
// It handles the game board and ships as mutable maps and lists, allowing for dynamic creation.
// The app will receive the game board and ship positions from this repository as a map of maps and a list of ships.
class GameBoardRepositoryImpl() : GameBoardRepository {
    private val gameBoardSize: Int = 10
    private var gameBoard: MutableList<MutableList<Int>> = MutableList(gameBoardSize) {
        MutableList(gameBoardSize) { 0 }
    }
    private var shipList: MutableList<Ship> = mutableListOf()

    // Function to set the position of the ships on the game board
    override fun createGameBoard(): Result<Unit> {
        return runCatching {
            // Reset the game board to a new state so that it can be reused
            gameBoard = MutableList(gameBoardSize) { MutableList(gameBoardSize) { 0 } }
            shipList.clear()

            val shipSizes = listOf(5, 4, 3, 3, 2)
            shipSizes.map { size ->
                shipList.add(setShipPosition(size))
            }
        }
    }

    override fun getGameBoard(): Map<String, Map<String, Int>> {
        return gameBoard.mapIndexed { rowIndex, row ->
            rowIndex.toString() to row.mapIndexed { colIndex, value ->
                colIndex.toString() to value
            }.toMap()
        }.toMap()
    }

    override fun getShipList(): List<Ship> {
        return shipList.toList()
    }

    // This function places the ship on the game board and returns the Ship object
    private fun setShipPosition(shipSize: Int) : Ship {
        val position = definePosition(shipSize)
        val shipBody = MutableList(shipSize) { ShipPiece(0, 0, false) }

        for (i in 0 until shipSize)
            when (position.shipDirection) {
                ShipDirection.VERTICAL -> {
                    gameBoard[position.x][position.y + i] = 1
                    shipBody[i] = shipBody[i].copy(x = position.x, y = position.y + i)
                }

                ShipDirection.HORIZONTAL -> {
                    gameBoard[position.x + i][position.y] = 1
                    shipBody[i] = shipBody[i].copy(x = position.x + i, y = position.y)
                }
            }
        return Ship(
            size = shipSize,
            shipBody = shipBody.toList(),
            sunk = false
        )
    }


    // Function to define a random position for a ship on the game board
    // This function tries different positions and directions until it finds a valid one
    private fun definePosition(shipSize: Int): ShipPosition {
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

            // Check if the random position is valid
            if (checkPosition(x0, x1, y0, y1)) {
                return ShipPosition(x0, y0, shipDirection)
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
                if (gameBoard[x][y] != 0) return false
            }
        }
        return true
    }
}