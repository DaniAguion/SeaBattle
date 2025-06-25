package com.example.seabattle.domain.usecase.presence

import com.example.seabattle.data.realtimedb.toPresenceError
import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.GameError
import com.example.seabattle.domain.errors.PresenceError
import com.example.seabattle.domain.errors.UserError
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
            if (throwable is PresenceError) throw throwable
            else throw DomainError.Unknown(throwable)
        }
    }
}