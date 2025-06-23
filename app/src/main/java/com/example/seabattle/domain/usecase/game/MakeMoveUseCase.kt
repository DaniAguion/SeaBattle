package com.example.seabattle.domain.usecase.game

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.entity.Ship
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.GameError
import com.example.seabattle.domain.errors.UserError
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
            fun makeMove(game: Game) : Map<String, Any?> {
                if (game.gameState != GameState.IN_PROGRESS.name) {
                    throw GameError.InvalidGameState()
                }
                if (game.currentPlayer != userId) {
                    throw UserError.UserProfileNotFound()
                }
                if (game.player1.userId != userId && game.player2.userId != userId) {
                    throw GameError.GameNotValid()
                }

                // Get the opponent ships and create a copy
                val currentShips = if (game.currentPlayer == game.player1.userId) {
                    game.player2Ships
                } else {
                    game.player1Ships
                }
                var updatedShips: List<Ship> = currentShips
                var gameState = game.gameState
                var winnerId = game.winnerId
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
                val row = gameBoard[x.toString()] ?: throw GameError.Unknown()
                val cellValue = row[y.toString()] ?: throw GameError.Unknown()

                if (gameBoard[x.toString()] == null || gameBoard[x.toString()]?.get(y.toString()) == null) {
                    throw GameError.Unknown()
                }

                var nextPlayer = ""

                when (cellValue) {
                    0 -> { // Did not hit. Turn over
                        gameBoard[x.toString()]?.put(y.toString(), 2)
                        nextPlayer = if (game.currentPlayer == game.player1.userId) game.player2.userId else game.player1.userId
                    }

                    1 -> { // Hit. Player continues. Update the game board and the ships status.
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

                            // If the ship was sunk, update all the pieces of the ship on the game board
                            if (isSunk) {
                                newShipBody.forEach { piece ->
                                    gameBoard[piece.x.toString()]?.put(piece.y.toString(), 5)
                                }
                            }

                            ship.copy(shipBody = newShipBody, sunk = isSunk)
                        }

                        // Check if the are any ships left
                        if (updatedShips.all { it.sunk }) {
                            gameState = GameState.GAME_FINISHED.name
                            winnerId = game.currentPlayer
                        }
                    }
                    else -> {
                        throw GameError.Unknown()
                    }
                }

                val movementData = if (game.currentPlayer == game.player1.userId) {
                    mapOf(
                        "boardForPlayer1" to gameBoard,
                        "player2Ships" to updatedShips,
                    )
                } else {
                    mapOf(
                        "boardForPlayer2" to gameBoard,
                        "player1Ships" to updatedShips,
                    )
                }


                return movementData + mapOf(
                    "currentMove" to game.currentMove + 1,
                    "currentPlayer" to nextPlayer,
                    "gameState" to gameState,
                    "winnerId" to winnerId,
                )
            }
            gameRepository.updateGameFields(gameId = gameId, logicFunction = ::makeMove).getOrThrow()
        }
        .onFailure { e ->
            Timber.e(e, "MakeMoveUseCase failed.")
        }
        .recoverCatching { throwable ->
            if (throwable is GameError) throw throwable
            else if (throwable is UserError) throw throwable
            else throw DomainError.Unknown(throwable)
        }
    }
}
