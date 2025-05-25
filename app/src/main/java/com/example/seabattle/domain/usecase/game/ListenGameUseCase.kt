package com.example.seabattle.domain.usecase.game


import com.example.seabattle.domain.Session
import com.example.seabattle.domain.repository.GameRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ListenGameUseCase(
    val gameRepository: GameRepository,
    val ioDispatcher: CoroutineDispatcher,
    val session: Session,
) {
    // This function listen the game entity and updates the local game object
    suspend operator fun invoke(gameId: String): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            if (gameId.isEmpty()) {
                throw IllegalStateException("Game is not set")
            }

            val flowCollector = gameRepository.listenGameUpdates(gameId)
                .map { result -> result.getOrThrow() }
                .collect { game ->
                    session.setCurrentGame(game)
                }
        }
    }
}