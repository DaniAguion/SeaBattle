package com.example.seabattle.domain.usecase.game


import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.GameError
import com.example.seabattle.domain.repository.GameRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class ListenGameUseCase(
    val gameRepository: GameRepository,
    val ioDispatcher: CoroutineDispatcher,
    val session: Session,
) {
    // This function listen the game entity and updates the local data
    operator fun invoke(gameId: String): Flow<Result<Game>> {
        return gameRepository.listenGameUpdates(gameId)
            .onEach { result ->
                result.onSuccess { game ->
                    session.setCurrentGame(game)
                }
                result.onFailure { throwable ->
                    Timber.e(throwable, "ListenGameUpdates failed.")
                }
            }
            .catch { throwable ->
                Timber.e(throwable, "ListenGameUseCase failed.")
                when (throwable) {
                    is GameError -> emit(Result.failure(throwable))
                    else -> emit(Result.failure(DomainError.Unknown(throwable)))
                }
            }
    }
}