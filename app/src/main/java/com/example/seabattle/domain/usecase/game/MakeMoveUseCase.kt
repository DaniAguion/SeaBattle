package com.example.seabattle.domain.usecase.game

import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.entity.CellState
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.entity.Ship
import com.example.seabattle.domain.errors.DataError
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.GameError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.GameRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber


class MakeMoveUseCase(
    val gameRepository: GameRepository,
    val sessionService: SessionService,
    val ioDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(x: Int, y: Int) : Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val userId = sessionService.getCurrentUserId()
            val gameId = sessionService.getCurrentGameId()

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
                    throw GameError.InvalidData()
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
                val row = gameBoard[x.toString()] ?: throw GameError.InvalidData()
                val cellValue = row[y.toString()] ?: throw GameError.InvalidData()

                if (gameBoard[x.toString()] == null || gameBoard[x.toString()]?.get(y.toString()) == null) {
                    throw GameError.InvalidData()
                }

                var nextPlayer = ""

                when (cellValue) {
                    CellState.HIDDEN_WATER.value -> { // Did not hit. Turn over
                        val cellNewValue = CellState.WATER.value
                        gameBoard[x.toString()]?.put(y.toString(), cellNewValue)
                        nextPlayer = if (game.currentPlayer == game.player1.userId) game.player2.userId else game.player1.userId
                    }

                    CellState.SHIP.value,
                    CellState.SHIP_TOP.value,
                    CellState.SHIP_BOTTOM.value,
                    CellState.SHIP_START.value,
                    CellState.SHIP_END.value -> {
                        // Hit. Player continues. Update the game board and the ships status.
                        val cellNewValue = if (cellValue == CellState.SHIP_TOP.value) {
                            CellState.HIT_TOP.value
                        } else if (cellValue == CellState.SHIP_BOTTOM.value) {
                            CellState.HIT_BOTTOM.value
                        } else if (cellValue == CellState.SHIP_START.value) {
                            CellState.HIT_START.value
                        } else if (cellValue == CellState.SHIP_END.value) {
                            CellState.HIT_END.value
                        } else {
                            CellState.HIT.value
                        }

                        gameBoard[x.toString()]?.put(y.toString(), cellNewValue)
                        nextPlayer = game.currentPlayer
                        updatedShips = currentShips.map { ship ->
                            val newShipBody = ship.shipBody.map { piece ->
                                if (piece.x.toString() == x.toString() && piece.y.toString() == y.toString()) {
                                    piece.copy(touched = true)
                                } else {
                                    piece
                                }
                            }
                            // Check if the ship is sunk and was not sunk before
                            val sunkNow = newShipBody.all { it.touched } && !ship.sunk

                            // If the ship was sunk, update all the pieces of the ship on the game board
                            if (sunkNow) {
                                newShipBody.forEach { piece ->
                                    val cellValue = gameBoard[piece.x.toString()]?.get(piece.y.toString()) ?: throw GameError.InvalidData()

                                    val sunkCellValue = if (cellValue == CellState.HIT_TOP.value || cellValue == CellState.SHIP_TOP.value) {
                                        CellState.SUNK_TOP.value
                                    } else if (cellValue == CellState.HIT_BOTTOM.value || cellValue == CellState.SHIP_BOTTOM.value) {
                                        CellState.SUNK_BOTTOM.value
                                    } else if (cellValue == CellState.HIT_START.value || cellValue == CellState.SHIP_START.value) {
                                        CellState.SUNK_START.value
                                    } else if (cellValue == CellState.HIT_END.value || cellValue == CellState.SHIP_END.value) {
                                        CellState.SUNK_END.value
                                    } else if (cellValue == CellState.HIT.value || cellValue == CellState.SHIP.value) {
                                        CellState.SUNK.value
                                    } else {
                                        cellValue
                                    }

                                    gameBoard[piece.x.toString()]?.put(piece.y.toString(), sunkCellValue)
                                }
                            }

                            ship.copy(shipBody = newShipBody, sunk = (sunkNow || ship.sunk))
                        }

                        // Check if the are any ships left
                        if (updatedShips.all { it.sunk }) {
                            gameState = GameState.GAME_FINISHED.name
                            winnerId = game.currentPlayer
                        }
                    }
                    else -> {
                        throw GameError.InvalidData()
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
            when (throwable) {
                is GameError -> throw throwable
                is UserError -> throw throwable
                is DataError -> throw throwable
                else -> throw DomainError.Unknown(throwable)
            }
        }
    }
}
