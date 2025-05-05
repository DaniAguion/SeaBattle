package com.example.seabattle.domain.usecase.auth

import com.example.seabattle.data.local.SecurePrefsData
import com.example.seabattle.domain.entity.LoginMethod
import com.example.seabattle.domain.repository.AuthRepository
import com.example.seabattle.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.getOrThrow


class LoginUserUseCase (
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val securePrefs: SecurePrefsData,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(loginMethod: LoginMethod): Result<Boolean>
    = withContext(ioDispatcher) {
        runCatching {
            val logged = authRepository.loginUser(loginMethod).getOrThrow()
            if (!logged) return@runCatching false

            val userProfile = authRepository.getAuthUserProfile()
                ?: throw IllegalStateException("User profile is null after login")

            // If user logged with Google, check if the user already exists in Firestore
            if (loginMethod is LoginMethod.Google) {
                val existingProfile = userRepository.getUser(userProfile.userId).getOrThrow()
                if (existingProfile == null) {
                    userRepository.createUser(userProfile).getOrThrow()
                }
            }
            securePrefs.saveUserSession(userProfile)
            return@runCatching true
        }
    }
}