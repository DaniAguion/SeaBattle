package com.example.seabattle.domain.usecase.user

import com.example.seabattle.domain.entity.User
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber


class FindUserUseCase(
    val userRepository: UserRepository,
    val ioDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(userName: String): Result<List<User>> = withContext(ioDispatcher) {
        runCatching {
            val foundUsers = userRepository.findUserByName(userName).getOrThrow()
            return@runCatching foundUsers
        }
        .onFailure { e ->
            Timber.e(e, "FindUserUseCase failed.")
        }
        .recoverCatching { throwable ->
            if (throwable is UserError) throw throwable
            else throw DomainError.Unknown(throwable)
        }
    }
}