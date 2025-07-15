package com.example.seabattle.domain.usecase.user

import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.errors.AuthError
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.AuthRepository
import com.example.seabattle.domain.repository.PresenceRepository
import com.example.seabattle.domain.repository.UserGamesRepository
import com.example.seabattle.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

class DeleteUserUseCase (
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val userGamesRepository: UserGamesRepository,
    private val presenceRepo: PresenceRepository,
    private val sessionService: SessionService,
    val ioDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke() = withContext(ioDispatcher) {
        runCatching {
            val userId = sessionService.getCurrentUserId()

            // Delete user from all repositories
            presenceRepo.deleteUserPresence(userId = userId).getOrThrow()
            userRepository.deleteUser(userId = userId).getOrThrow()
            userGamesRepository.deleteUserGames(userId = userId).getOrThrow()
            authRepository.deleteUser().getOrThrow()
            sessionService.clearCurrentUserId()
        }
        .onFailure { e ->
            Timber.e(e, "DeleteUserUseCase failed.")
        }
        .recoverCatching { throwable ->
            if (throwable is UserError) throw throwable
            else if (throwable is AuthError) throw throwable
            else throw DomainError.Unknown(throwable)
        }
    }
}