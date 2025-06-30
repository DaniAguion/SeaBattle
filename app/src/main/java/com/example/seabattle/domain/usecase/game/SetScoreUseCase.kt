package com.example.seabattle.domain.usecase.game

import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.GameError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.ScoreRepository
import com.example.seabattle.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

class SetScoreUseCase(
    val userRepository: UserRepository,
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
            val user = userRepository.getUser(userId).getOrThrow()
            return@runCatching user.score
        }
            .onFailure { e ->
                Timber.Forest.e(e, "SetScoreUseCase failed.")
            }
            .recoverCatching { throwable ->
                if (throwable is GameError) throw throwable
                else if (throwable is UserError) throw throwable
                else throw DomainError.Unknown(throwable)
            }
    }
}