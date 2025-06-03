package com.example.seabattle.domain.usecase.game

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.entity.Ship
import com.example.seabattle.domain.repository.GameRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber


class MakeMoveUseCase(
    val gameRepository: GameRepository,
    val session: Session,
    val ioDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(x: Int, y: Int) : Result<Unit> = withContext(ioDispatcher) {
        runCatching {
        val userId = session.getCurrentUserId()
        val gameId = session.getCurrentGameId()

        if (userId.isEmpty() || gameId.isEmpty()) {
            throw IllegalStateException("User is not logged in or game is not set")
        }

        // Function to validate game state, make a move and calculate the result
        fun makeMove(game: Game) : Map<String, Any> {
            if (game.gameState != GameState.IN_PROGRESS.name) {
                throw Exception("Game is not in progress state")
            }
            if (game.currentPlayer != userId) {
                throw Exception("It's not user turn")
            }

            if (game.player1.userId != userId && game.player2.userId != userId) {
                throw Exception("User does not belong to this game")
            }

            // Get the opponent ships and create a copy
            val currentShips = if (game.currentPlayer == game.player1.userId) {
                game.player2Ships
            } else {
                game.player1Ships
            }
            var updatedShips: List<Ship> = currentShips

            // Deep copy of game board and ships and convert them to mutable maps
            // This is necessary to avoid modifying the original game object
            // Its converted to mutable maps to allow modifications
            val gameBoard = if (game.currentPlayer == game.player1.userId) {
                game.boardForPlayer1.mapValues { (_, innerMap) -> innerMap.toMutableMap() }
                    .toMutableMap()
            } else {
                game.boardForPlayer2.mapValues { (_, innerMap) -> innerMap.toMutableMap() }
                    .toMutableMap()
            }


            // Check valid cell coordinates
            val row = gameBoard[x.toString()] ?: throw Exception("Invalid cell coordinates")
            val cellValue = row[y.toString()] ?: throw Exception("Invalid cell coordinates")

            if (gameBoard[x.toString()] == null || gameBoard[x.toString()]?.get(y.toString()) == null) {
                throw Exception("Invalid cell coordinates")
            }

            var nextPlayer = ""

            when (cellValue) {
                0 -> { // Did not hit. Turn over
                    gameBoard[x.toString()]?.put(y.toString(), 2)
                    nextPlayer = if (game.currentPlayer == game.player1.userId) game.player2.userId else game.player1.userId
                }

                1 -> { // Hit. Player continues. Check if ship is sunk.
                    gameBoard[x.toString()]?.put(y.toString(), 3)
                    nextPlayer = game.currentPlayer
                    updatedShips = currentShips.map { ship ->
                        val newShipBody = ship.shipBody.map { piece ->
                            if (piece.x.toString() == x.toString() && piece.y.toString() == y.toString()) {
                                piece.copy(touched = true)
                            } else {
                                piece
                            }
                        }
                        val isSunk = newShipBody.all { it.touched }
                        ship.copy(shipBody = newShipBody, sunk = isSunk)
                    }
                }
                else -> {
                    throw Exception("Invalid cell value")
                }
            }

            return mapOf(
                "boardForPlayer1" to if (game.currentPlayer == game.player1.userId) gameBoard else game.boardForPlayer1,
                "player1Ships" to if (game.currentPlayer == game.player1.userId) updatedShips else game.player1Ships,
                "boardForPlayer2" to if (game.currentPlayer == game.player2.userId) gameBoard else game.boardForPlayer2,
                "player2Ships" to if (game.currentPlayer == game.player2.userId) updatedShips else game.player2Ships,
                "currentTurn" to game.currentTurn + 1,
                "currentPlayer" to nextPlayer
            )
        }

            gameRepository.updateGameFields(gameId = gameId, logicFunction = ::makeMove).getOrThrow()
        }
    }
}