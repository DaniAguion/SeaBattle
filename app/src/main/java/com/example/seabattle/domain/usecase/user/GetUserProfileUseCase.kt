package com.example.seabattle.domain.usecase.user

import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.entity.User
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber


class GetUserProfileUseCase(
    val userRepository: UserRepository,
    val sessionService: SessionService,
    val ioDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(): Result<User> = withContext(ioDispatcher) {
        runCatching {
            val userId = sessionService.getCurrentUserId()

            if (userId.isEmpty()) {
                throw UserError.UserProfileNotFound()
            }

            // Return the score based on the userId
            val user = userRepository.getUserById(userId).getOrThrow()
            return@runCatching user
        }
        .onFailure { e ->
            Timber.e(e, "GetUserProfile failed.")
        }
        .recoverCatching { throwable ->
            if (throwable is UserError) throw throwable
            else throw DomainError.Unknown(throwable)
        }
    }
}