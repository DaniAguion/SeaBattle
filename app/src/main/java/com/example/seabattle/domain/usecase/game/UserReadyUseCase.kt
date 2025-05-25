package com.example.seabattle.domain.usecase.game

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.repository.GameRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext


class UserReadyUseCase(
    val gameRepository: GameRepository,
    val session: Session,
    val ioDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val playerId = session.getCurrentUserId()
            val gameId = session.getCurrentGameId()

            if (playerId.isEmpty() || gameId.isEmpty()) {
                throw IllegalStateException("User is not logged in or game is not set")
            }

            gameRepository.updateUserReady(gameId, playerId).getOrThrow()
        }
    }
}