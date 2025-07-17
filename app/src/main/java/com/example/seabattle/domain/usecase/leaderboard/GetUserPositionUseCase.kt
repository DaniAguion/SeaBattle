package com.example.seabattle.domain.usecase.leaderboard

import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

class GetUserPositionUseCase(
    val sessionService: SessionService,
    val userRepository: UserRepository,
    val ioDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(): Result<Int> = withContext(ioDispatcher) {
        runCatching {
            val userId = sessionService.getCurrentUserId()
            val user = userRepository.getUserById(userId).getOrThrow()

            val userPosition = userRepository.getUserPosition(userId = userId, userScore = user.score ).getOrThrow()
            return@runCatching userPosition
        }
            .onFailure { e ->
                Timber.Forest.e(e, "GetUserPositionUseCase failed.")
            }
            .recoverCatching { throwable ->
                if (throwable is UserError) throw throwable
                else throw DomainError.Unknown(throwable)
            }
    }
}