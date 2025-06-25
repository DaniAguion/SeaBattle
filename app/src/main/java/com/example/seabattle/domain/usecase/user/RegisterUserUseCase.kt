package com.example.seabattle.domain.usecase.user

import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.repository.AuthRepository
import com.example.seabattle.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

class RegisterUserUseCase (
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val sessionService: SessionService,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        username: String,
        email: String,
        password: String ): Result<Boolean>
    = withContext(ioDispatcher) {
        runCatching {
            val registered = authRepository.registerUser(email, password).getOrThrow()
            if (!registered) return@runCatching false

            authRepository.setUserName(username).getOrThrow()

            val userProfile = authRepository.getAuthUserProfile().getOrThrow()

            userRepository.createUser(userProfile).getOrThrow()
            sessionService.setCurrentUser(userProfile)
            return@runCatching true
        }
        .onFailure { e ->
            Timber.e(e, "RegisterUserUseCase failed.")
        }
    }
}

