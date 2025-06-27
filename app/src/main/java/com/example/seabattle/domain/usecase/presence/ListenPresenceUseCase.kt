package com.example.seabattle.domain.usecase.presence

import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.PresenceError
import com.example.seabattle.domain.repository.PresenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class ListenPresenceUseCase (
    private val presenceRepo: PresenceRepository,
    private val sessionService: SessionService,
) {
    operator fun invoke(): Flow<Result<String>>  {
        val userId = sessionService.getCurrentUserId()

        if (userId == "") {
            return flowOf(Result.failure(PresenceError.UserNotAuthenticated()))
        }

        return presenceRepo.listenUserPresence(userId)
            .onEach { result ->
                result.onFailure { throwable ->
                    Timber.e(throwable, "ListenPresenceUseCase failed.")
                }
            }
            .catch { throwable ->
                Timber.e(throwable, "ListenPresenceUseCase failed.")
                when (throwable) {
                    is PresenceError -> emit(Result.failure(throwable))
                    else -> emit(Result.failure(DomainError.Unknown(throwable)))
                }
            }
    }
}