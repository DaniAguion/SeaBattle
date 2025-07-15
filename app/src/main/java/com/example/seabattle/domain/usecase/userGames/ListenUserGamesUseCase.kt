package com.example.seabattle.domain.usecase.userGames

import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.entity.UserGames
import com.example.seabattle.domain.errors.DataError
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.GameError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.UserGamesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber


class ListenUserGamesUseCase(
    val userGamesRepository: UserGamesRepository,
    val sessionService: SessionService,
    val ioDispatcher: CoroutineDispatcher,
) {
    operator fun invoke(): Flow<Result<UserGames>> = flow {
        val userId = sessionService.getCurrentUserId()

        if (userId.isEmpty()) {
            emit(Result.failure(UserError.UserProfileNotFound()))
            return@flow
        }

        userGamesRepository.listenToUserGames(userId)
            .catch { throwable ->
                Timber.e(throwable, "ListenGameUseCase failed.")
                when (throwable) {
                    is GameError -> emit(Result.failure(throwable))
                    is DataError -> emit(Result.failure(throwable))
                    else -> emit(Result.failure(DomainError.Unknown(throwable)))
                }
            }
            .collect { emit(it) }
    }
}