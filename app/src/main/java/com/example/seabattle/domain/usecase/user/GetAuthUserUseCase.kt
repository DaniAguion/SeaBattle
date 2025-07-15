package com.example.seabattle.domain.usecase.user

import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.entity.User
import com.example.seabattle.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.getOrThrow
import kotlin.onFailure
import kotlin.runCatching

class GetAuthUserUseCase (
    private val authRepository: AuthRepository,
    private val sessionService: SessionService,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): Result<User?> = withContext(ioDispatcher) {
        runCatching {
            if (!authRepository.isLoggedIn()) {
                return@runCatching null
            }

            val userProfile = authRepository.getAuthUserProfile().getOrThrow()
            sessionService.setCurrentUserId(userProfile.userId)
            return@runCatching userProfile
        }
            .onFailure { e ->
                Timber.e(e, "GetAuthUserUseCase failed.")
            }
    }
}