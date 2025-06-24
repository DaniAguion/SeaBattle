package com.example.seabattle.domain.usecase.game

import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.GameError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.GameRepository
import com.example.seabattle.domain.repository.ScoreRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber


class SetScoreUseCase(
    val gameRepository: GameRepository,
    val scoreRepository: ScoreRepository,
    val sessionService: SessionService,
    val ioDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(gameId: String): Result<Int> = withContext(ioDispatcher) {
        runCatching {
            val userId = sessionService.getCurrentUserId()

            if (userId.isEmpty()) {
                throw UserError.UserProfileNotFound()
            }

            if (gameId.isEmpty()) {
                throw GameError.GameNotFound()
            }

            // Call the function to  get the game details
            scoreRepository.updateScore(gameId).getOrThrow()

            // Return the score based on the userId
            return@runCatching when (userId) {
                gameRepository.getGame(gameId).getOrThrow().player1.userId -> {
                    gameRepository.getGame(gameId).getOrThrow().player1.score
                }
                gameRepository.getGame(gameId).getOrThrow().player2.userId -> {
                    gameRepository.getGame(gameId).getOrThrow().player2.score
                }
                else -> throw UserError.UserProfileNotFound()
            }
        }
        .onFailure { e ->
            Timber.e(e, "SetScoreUseCase failed.")
        }
        .recoverCatching { throwable ->
            if (throwable is GameError) throw throwable
            else if (throwable is UserError) throw throwable
            else throw DomainError.Unknown(throwable)
        }
    }
}