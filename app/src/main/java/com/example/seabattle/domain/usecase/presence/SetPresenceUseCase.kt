package com.example.seabattle.domain.usecase.presence

import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.PresenceError
import com.example.seabattle.domain.repository.PresenceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

class SetPresenceUseCase (
    private val presenceRepo: PresenceRepository,
    private val sessionService: SessionService,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val userId = sessionService.getCurrentUserId()
            if (userId == "") {
                throw PresenceError.UserNotAuthenticated()
            }
            presenceRepo.definePresence(userId = userId).getOrThrow()
        }
        .onFailure { e ->
            Timber.e(e, "SetPresenceUseCase failed.")
        }
        .recoverCatching { throwable ->
            if (throwable is PresenceError) throw throwable
            else throw DomainError.Unknown(throwable)
        }
    }
}