package com.example.seabattle.domain.usecase.presence

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.repository.PresenceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

class SetPresenceUseCase (
    private val presenceRepo: PresenceRepository,
    private val session: Session,
    private val ioDispatcher: CoroutineDispatcher
) {
    operator suspend fun invoke(): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val userId = session.getCurrentUserId()
            presenceRepo.definePresence(userId = userId).getOrThrow()
            presenceRepo.listenUserPresence(userId).collect { result ->
                result.onSuccess { status ->
                    Timber.d("User presence status is $status.")
                }.onFailure { error ->
                    Timber.e(error, "Failed to listen user presence.")
                }
            }
        }
        .onFailure { e ->
            Timber.e(e, "SetPresenceUseCase failed.")
        }
        .recoverCatching { throwable ->
            throw DomainError.PresenceError()
        }
    }
}