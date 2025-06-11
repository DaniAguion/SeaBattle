package com.example.seabattle.domain.usecase.game

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.GameError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.GameRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber


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

            if (userId.isEmpty()) {
                throw UserError.UserProfileNotFound()
            }

            if (gameId.isEmpty() || game == null) {
                throw GameError.GameNotFound()
            }

            if (game.gameState == GameState.GAME_ABORTED.name || game.gameState == GameState.GAME_FINISHED.name) {
                // If the game is already finished or aborted, we can just delete it
                gameRepository.deleteGame(gameId).getOrThrow()

            } else {
                // If the game is in progress or waiting, the game state has to be updated to reflect the user leaving
                fun leaveGame(game: Game): Map<String, Any> {
                    if (game.gameState == GameState.WAITING_FOR_PLAYER.name || game.gameState == GameState.CHECK_READY.name) {
                        return mapOf(
                            "gameState" to GameState.GAME_ABORTED.name,
                        )
                    } else if (game.gameState == GameState.IN_PROGRESS.name) {
                        val winnerId = if (game.player1.userId == userId) game.player2.userId else game.player1.userId
                        return mapOf(
                            "gameState" to GameState.GAME_FINISHED.name,
                            "winnerId" to winnerId
                        )
                    } else {
                        throw GameError.InvalidGameState()
                    }
                }
                gameRepository.updateGameFields(gameId = gameId, logicFunction = ::leaveGame).getOrThrow()
            }
            session.clearCurrentGame()
        }
        .onFailure { e ->
            Timber.e(e, "LeaveGameUseCase failed.")
        }
        .recoverCatching { throwable ->
            if (throwable is GameError) throw throwable
            else if (throwable is UserError) throw throwable
            else throw DomainError.Unknown(throwable)
        }
    }
}