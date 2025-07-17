package com.example.seabattle.domain.usecase.user

import com.example.seabattle.domain.entity.Player
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber


class FindPlayerUseCase(
    val userRepository: UserRepository,
    val ioDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(userName: String): Result<List<Player>> = withContext(ioDispatcher) {
        runCatching {
            val foundUsers = userRepository.findPlayerByName(userName).getOrThrow()
            return@runCatching foundUsers
        }
        .onFailure { e ->
            Timber.e(e, "FindPlayerUseCase failed.")
        }
        .recoverCatching { throwable ->
            if (throwable is UserError) throw throwable
            else throw DomainError.Unknown(throwable)
        }
    }
}