package com.example.seabattle.domain.usecase.auth

import com.example.seabattle.data.local.SecurePrefsData
import com.example.seabattle.domain.repository.AuthRepository
import com.example.seabattle.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RegisterUserUseCase (
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val securePrefs: SecurePrefsData,
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

            val userProfile = authRepository.getAuthUserProfile()
                ?: throw IllegalStateException("User profile is null after registration")

            userRepository.createUser(userProfile).getOrThrow()
            securePrefs.saveUserSession(userProfile)
            return@runCatching true
        }
    }
}

