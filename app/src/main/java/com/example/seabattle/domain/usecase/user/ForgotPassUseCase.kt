package com.example.seabattle.domain.usecase.user


import com.example.seabattle.domain.errors.AuthError
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

class ForgotPassUseCase (
    private val authRepository: AuthRepository,
    val ioDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(email: String) = withContext(ioDispatcher) {
        runCatching {
            authRepository.askForPasswordReset(email = email).getOrThrow()
        }
        .onFailure { e ->
            Timber.e(e, "ForgotPassUseCase failed.")
        }
        .recoverCatching { throwable ->
            if (throwable is AuthError) throw throwable
            else throw DomainError.Unknown(throwable)
        }
    }
}