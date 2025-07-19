package com.example.seabattle.domain.usecase.user

import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.errors.AuthError
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.AuthRepository
import com.example.seabattle.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

class ChangeUserNameUseCase (
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val sessionService: SessionService,
    val ioDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(newUserName: String): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val userId = sessionService.getCurrentUserId()
            userRepository.changeUserName(userId, newUserName).getOrThrow()
            authRepository.setUserName(newUserName).getOrThrow()
        }
        .onFailure { e ->
            Timber.e(e, "ChangeUserNameUseCase failed.")
        }
        .recoverCatching { throwable ->
            if (throwable is UserError) throw throwable
            else if (throwable is AuthError) throw throwable
            else throw DomainError.Unknown(throwable)
        }
    }
}