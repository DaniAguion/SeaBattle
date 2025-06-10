package com.example.seabattle.domain.usecase.game

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.GameError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.GameRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber


class ClaimVictoryUseCase(
    val gameRepository: GameRepository,
    val session: Session,
    val ioDispatcher: CoroutineDispatcher,
) {

    internal fun claimVictoryConditions(userId: String, game: Game) : Boolean {
        // First, check if the game is in progress and the current player is not the user
        if (game.gameState != GameState.IN_PROGRESS.name) return false
        if (game.currentPlayer == userId) return false

        // If the current player is offline for more than 30 seconds
        // or the game has not been updated for more than 1 minute, claim victory
        val currentPlayerOffline = ((game.currentPlayer == game.player1.userId && game.player1.status != "online") ||
                (game.currentPlayer == game.player2.userId && game.player2.status != "online"))
        val updatedAtTime = game.updatedAt?.time
        if (updatedAtTime == null) throw GameError.GameNotValid()
        val lastUpdate = (System.currentTimeMillis() - updatedAtTime)/ 1000
        return (currentPlayerOffline && lastUpdate > 30) || (lastUpdate > 60)
    }


    suspend operator fun invoke(): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val userId = session.getCurrentUserId()
            val gameId = session.getCurrentGameId()

            if (userId.isEmpty()) {
                throw UserError.UserProfileNotFound()
            }

            if (gameId.isEmpty()) {
                throw GameError.GameNotFound()
            }

            // Check if the game is in the correct state for user readiness and change the state accordingly
            fun checkUserAFK(game: Game) : Map<String, Any> {
                if (claimVictoryConditions(userId = userId, game = game)) {
                    return mapOf(
                        "winner" to userId,
                        "gameState" to GameState.USER_LEFT.name
                    )
                } else throw GameError.InvalidGameState()
            }

            gameRepository.updateGameFields(gameId = gameId, logicFunction = ::checkUserAFK).getOrThrow()
        }
        .onFailure { e ->
            Timber.e(e, "UserReadyUseCase failed.")
        }
        .recoverCatching { throwable ->
            if (throwable is GameError) throw throwable
            else if (throwable is UserError) throw throwable
            else throw DomainError.Unknown(throwable)
        }
    }
}