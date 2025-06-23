package com.example.seabattle.domain.usecase.game

import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.GameError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.GameRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber


class UserReadyUseCase(
    val gameRepository: GameRepository,
    val sessionService: SessionService,
    val ioDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val userId = sessionService.getCurrentUserId()
            val gameId = sessionService.getCurrentGameId()

            if (userId.isEmpty()) {
                throw UserError.UserProfileNotFound()
            }

            if (gameId.isEmpty()) {
                throw GameError.GameNotFound()
            }

            // Check if the game is in the correct state for user readiness and change the state accordingly
            fun setUserReady(game: Game) : Map<String, Any> {
                if (game.gameState != GameState.CHECK_READY.name) {
                    throw GameError.InvalidGameState()
                }

                val player1Ready = if (userId == game.player1.userId) true else game.player1Ready
                val player2Ready = if (userId == game.player2.userId) true else game.player2Ready
                val gameState = if (player1Ready && player2Ready) GameState.IN_PROGRESS.name else game.gameState

                return mapOf(
                    "player1Ready" to player1Ready,
                    "player2Ready" to player2Ready,
                    "gameState" to gameState
                )
            }

            gameRepository.updateGameFields(gameId = gameId, logicFunction = ::setUserReady).getOrThrow()
        }
        .onFailure { e ->
            Timber.e(e, "UserReadyUseCase failed.")
        }
        .recoverCatching { throwable ->
            if (throwable is GameError) throw throwable
            else if (throwable is UserError) throw throwable
            else throw DomainError.Unknown(throwable)
        }
    }
}