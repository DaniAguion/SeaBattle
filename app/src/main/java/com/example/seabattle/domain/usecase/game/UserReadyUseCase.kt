package com.example.seabattle.domain.usecase.game

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.GameState
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
            val userId = session.getCurrentUserId()
            val gameId = session.getCurrentGameId()
            val game = session.getCurrentGame()

            if (userId.isEmpty() || gameId.isEmpty() || game == null) {
                throw IllegalStateException("User is not logged in or game is not set")
            }

            if (game.gameState != GameState.CHECK_READY.name) {
                throw Exception("Game is not in check ready state")
            }

            val player1Ready = if (userId == game.player1.userId) true else game.player1Ready
            val player2Ready = if (userId == game.player2.userId) true else game.player2Ready

            // Create a game copy with the calculated data
            val newGame = game.copy(
                player1Ready = player1Ready,
                player2Ready = player2Ready,
                gameState = if (player1Ready && player2Ready) GameState.IN_PROGRESS.name else game.gameState,
            )

            gameRepository.updateGame(game= game, updatedGame= newGame).getOrThrow()
        }
    }
}