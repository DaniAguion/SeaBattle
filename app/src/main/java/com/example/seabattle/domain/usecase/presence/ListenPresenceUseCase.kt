package com.example.seabattle.domain.usecase.presence

import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.PresenceError
import com.example.seabattle.domain.repository.PresenceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

class ListenPresenceUseCase (
    private val presenceRepo: PresenceRepository,
    private val sessionService: SessionService,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): Result<String?> = withContext(ioDispatcher) {
        runCatching {
            var userStatus: String? = null
            val userId = sessionService.getCurrentUserId()

            if (userId == "") {
                throw PresenceError.UserNotAuthenticated()
            }

            presenceRepo.listenUserPresence(userId).collect { result ->
                result.onSuccess { status ->
                    Timber.d("User presence status is $status.")
                    userStatus = status
                }
                .onFailure { error ->
                    Timber.e(error, "Failed to listen user presence.")
                    userStatus = null
                }
            }

            Timber.d("Returning user presence status: $userStatus")
            return@runCatching userStatus
        }
        .onFailure { e ->
            Timber.e(e, "ListenPresenceUseCase failed.")
        }
        .recoverCatching { throwable ->
            if (throwable is PresenceError) throw throwable
            else throw DomainError.Unknown(throwable)
        }
    }
}