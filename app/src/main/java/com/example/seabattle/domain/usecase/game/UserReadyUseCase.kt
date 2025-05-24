package com.example.seabattle.domain.usecase.game

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.repository.GameRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


class UserReadyUseCase(
    val gameRepository: GameRepository,
    val session: Session,
    val ioDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val playerId = session.getCurrentUserId()
            val gameId = session.getCurrentGameId()

            if (playerId.isEmpty() || gameId.isEmpty()) {
                throw IllegalStateException("User is not logged in or game is not set")
            }

            var newData : Map<String, Any>

            val flowCollector = gameRepository.getGameUpdate(gameId)
                .map { result -> result.getOrThrow() }
                .first { game ->
                    when (playerId) {
                        game.player1.userId -> {
                            newData = mapOf(
                                "player1Ready" to true,
                            )
                            if (game.player2Ready) {
                                newData = newData + mapOf(
                                    "gameState" to GameState.IN_PROGRESS.name,
                                )
                            }
                        }

                        game.player2.userId -> {
                            newData = mapOf(
                                "player2Ready" to true,
                            )
                            if (game.player1Ready) {
                                newData = newData + mapOf(
                                    "gameState" to GameState.IN_PROGRESS.name,
                                )
                            }
                        }
                        else -> throw IllegalStateException("User doesn't belong to this room")
                    }
                    gameRepository.updateGame(game.gameId, newData).getOrThrow()
                    val newGame = gameRepository.getGame(game.gameId).getOrThrow()
                    session.setCurrentGame(newGame)
                    return@first true
                }

        }
    }
}