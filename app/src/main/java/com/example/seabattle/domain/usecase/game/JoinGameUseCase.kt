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


class JoinGameUseCase(
    val gameRepository: GameRepository,
    val session: Session,
    val ioDispatcher: CoroutineDispatcher,
) {
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
            fun setUserJoined(game: Game) : Map<String, Any> {
                if (game.gameState != GameState.WAITING_FOR_PLAYERS.name) {
                    throw GameError.InvalidGameState()
                }

                val player1Joined = if (userId == game.player1.userId) true else game.player1Joined
                val player2Joined = if (userId == game.player2.userId) true else game.player2Joined
                val gameState = if (player1Joined && player2Joined) GameState.CHECK_READY.name else game.gameState

                return mapOf(
                    "player1Joined" to player1Joined,
                    "player2Joined" to player2Joined,
                    "gameState" to gameState
                )
            }

            gameRepository.updateGameFields(gameId = gameId, logicFunction = ::setUserJoined).getOrThrow()
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