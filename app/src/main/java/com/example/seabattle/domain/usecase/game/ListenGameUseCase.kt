package com.example.seabattle.domain.usecase.game

import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.GameError
import com.example.seabattle.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import timber.log.Timber

class ListenGameUseCase(
    val gameRepository: GameRepository,
) {
    operator fun invoke(gameId: String): Flow<Result<Game>> {
        return gameRepository.listenGameUpdates(gameId)
            .catch { throwable ->
                Timber.e(throwable, "ListenGameUseCase failed.")
                when (throwable) {
                    is GameError -> emit(Result.failure(throwable))
                    else -> emit(Result.failure(DomainError.Unknown(throwable)))
                }
            }
    }
}