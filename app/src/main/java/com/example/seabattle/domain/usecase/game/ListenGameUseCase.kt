package com.example.seabattle.domain.usecase.game


import com.example.seabattle.domain.Session
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.GameError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.GameRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber

class ListenGameUseCase(
    val gameRepository: GameRepository,
    val ioDispatcher: CoroutineDispatcher,
    val session: Session,
) {
    // This function listen the game entity and updates the local game object
    suspend operator fun invoke(gameId: String): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            if (gameId.isEmpty()) {
                throw GameError.GameNotFound()
            }

            // Listen to game updates and update the session's current game
            gameRepository.listenGameUpdates(gameId)
                .map { result -> result.getOrThrow() }
                .collect { game ->
                    session.setCurrentGame(game)
                }
        }
        .onFailure { e ->
            Timber.e(e, "ListenGameUseCase failed.")
        }
        .recoverCatching { throwable ->
            if (throwable is GameError) throw throwable
            else throw DomainError.Unknown(throwable)
        }
    }
}