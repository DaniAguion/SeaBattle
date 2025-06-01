package com.example.seabattle.domain.usecase.game

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.repository.GameRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext


class LeaveGameUseCase(
    val gameRepository: GameRepository,
    val session: Session,
    val ioDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val userId = session.getCurrentUserId()
            val gameId = session.getCurrentGameId()
            val game = session.getCurrentGame()

            if (userId.isEmpty() || gameId.isEmpty() || game == null) {
                throw IllegalStateException("User is not logged in or game is not set")
            }

            if (game.gameState == GameState.GAME_ABORTED.name ||
                game.gameState == GameState.GAME_ABANDONED.name ||
                game.gameState == GameState.GAME_FINISHED.name) {
                // If the game is already finished or aborted, we can just delete it
                gameRepository.deleteGame(gameId).getOrThrow()

            } else {

                // If the game is in progress or waiting, the game state has to be updated to reflect the user leaving
                fun leaveGame(game: Game): Map<String, Any> {
                    if (game.gameState == GameState.CHECK_READY.name) {
                        return mapOf(
                            "gameState" to GameState.GAME_ABORTED.name,
                        )
                    } else if (game.gameState == GameState.IN_PROGRESS.name) {
                        val winnerId = if (game.player1.userId == userId) game.player2.userId else game.player1.userId
                        return mapOf(
                            "gameState" to GameState.USER_LEFT.name,
                            "winnerId" to winnerId
                        )
                    } else {
                        throw IllegalStateException("Game is not in a valid state to leave")
                    }
                }
                gameRepository.updateGameFields(gameId = gameId, logicFunction = ::leaveGame).getOrThrow()
            }

            session.clearCurrentGame()
        }
    }
}