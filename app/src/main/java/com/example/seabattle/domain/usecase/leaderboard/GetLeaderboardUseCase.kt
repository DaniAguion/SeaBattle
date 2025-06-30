package com.example.seabattle.domain.usecase.leaderboard

import com.example.seabattle.domain.entity.User
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

class GetLeaderboardUseCase(
    val userRepository: UserRepository,
    val ioDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(): Result<List<User>> = withContext(ioDispatcher) {
        runCatching {
            val userList = userRepository.getLeaderboard().getOrThrow()
            return@runCatching userList
        }
            .onFailure { e ->
                Timber.Forest.e(e, "GetLeaderboardUseCase failed.")
            }
            .recoverCatching { throwable ->
                if (throwable is UserError) throw throwable
                else throw DomainError.Unknown(throwable)
            }
    }
}