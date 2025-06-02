package com.example.seabattle.domain.usecase.auth

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.LoginMethod
import com.example.seabattle.domain.repository.AuthRepository
import com.example.seabattle.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.getOrThrow


class LoginUserUseCase (
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val session: Session,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(loginMethod: LoginMethod): Result<Boolean> = withContext(ioDispatcher) {
        runCatching {
            val logged = authRepository.loginUser(loginMethod).getOrThrow()
            if (!logged) return@runCatching false

            val userProfile = authRepository.getAuthUserProfile().getOrThrow()

            // If user logged with Google, check if the user already exists in Firestore
            if (loginMethod is LoginMethod.Google) {
                val existingProfile = userRepository.getUser(userProfile.userId).getOrNull()
                if (existingProfile == null) {
                    userRepository.createUser(userProfile).getOrThrow()
                }
            }
            session.setCurrentUser(userProfile)
            return@runCatching true
        }
        .onFailure { e ->
            Timber.e(e, "LoginUserUseCase failed.")
        }
    }
}