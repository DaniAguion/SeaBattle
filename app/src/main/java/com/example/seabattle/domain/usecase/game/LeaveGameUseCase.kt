package com.example.seabattle.domain.usecase.game

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.repository.GameRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext


class LeaveGameUseCase(
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


            if (game.gameState == GameState.GAME_ABORTED.name ||
                game.gameState == GameState.GAME_ABANDONED.name ||
                game.gameState == GameState.GAME_FINISHED.name) {
                // If the game is already finished or aborted, we can just delete it
                gameRepository.deleteGame(gameId).getOrThrow()
            } else if(game.gameState == GameState.CHECK_READY.name) {
                // If the game is in CHECK_READY state, the game can be aborted
                 val updatedFields = mapOf(
                    "gameState" to GameState.GAME_ABORTED.name,
                )
                gameRepository.updateGameField(gameId, updatedFields).getOrThrow()
            } else {
                // If the game is left in progress, the opponent is declared the winner
                val updatedGame = game.copy(
                    gameState = GameState.USER_LEFT.name,
                    winnerId = if (game.player1.userId == userId) game.player2.userId else game.player1.userId,
                )
                gameRepository.updateGame(game, updatedGame).getOrThrow()
            }
            session.clearCurrentGame()
        }
    }
}