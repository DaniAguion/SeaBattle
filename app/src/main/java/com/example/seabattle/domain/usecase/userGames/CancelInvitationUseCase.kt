package com.example.seabattle.domain.usecase.userGames

import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.errors.DataError
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.UserGamesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber


class CancelInvitationUseCase(
    val userGamesRepository: UserGamesRepository,
    val ioDispatcher: CoroutineDispatcher,
    val sessionService: SessionService,
) {
    suspend operator fun invoke(): Result<Unit>
    = withContext(ioDispatcher) {
        runCatching {
            val userId = sessionService.getCurrentUserId()
            userGamesRepository.cancelInvitation(userId).getOrThrow()
        }
        .onFailure { e ->
            Timber.e(e, "CancelInvitationUseCase failed.")
        }
        .recoverCatching { throwable ->
            when (throwable) {
                is UserError -> throw throwable
                is DataError -> throw throwable
                else -> throw DomainError.Unknown(throwable)
            }
        }
    }
}