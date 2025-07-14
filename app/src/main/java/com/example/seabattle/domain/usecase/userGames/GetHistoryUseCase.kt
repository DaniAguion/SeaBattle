package com.example.seabattle.domain.usecase.userGames

import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.entity.GameHistory
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.UserGamesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber


class GetHistoryUseCase(
    val userGamesRepository: UserGamesRepository,
    val sessionService: SessionService,
    val ioDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(): Result<List<GameHistory>> = withContext(ioDispatcher) {
        runCatching {
            val userId = sessionService.getCurrentUserId()

            if (userId.isEmpty()) {
                throw UserError.UserProfileNotFound()
            }

            // Return the score based on the userId
            val userGames = userGamesRepository.getUserGames(userId).getOrThrow()
            return@runCatching userGames.history.reversed()
        }
        .onFailure { e ->
            Timber.e(e, "GetHistoryUseCase failed.")
        }
        .recoverCatching { throwable ->
            if (throwable is UserError) throw throwable
            else throw DomainError.Unknown(throwable)
        }
    }
}