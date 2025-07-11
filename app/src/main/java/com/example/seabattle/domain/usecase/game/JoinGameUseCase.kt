package com.example.seabattle.domain.usecase.game

import com.example.seabattle.data.firestore.mappers.toDto
import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.errors.DataError
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.GameError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.GameRepository
import com.example.seabattle.domain.repository.UserGamesRepository
import com.example.seabattle.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber


class JoinGameUseCase(
    val gameRepository: GameRepository,
    val userRepository: UserRepository,
    val userGamesRepository: UserGamesRepository,
    val ioDispatcher: CoroutineDispatcher,
    val sessionService: SessionService,
) {
    suspend operator fun invoke(gameId: String): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val userId = sessionService.getCurrentUserId()
            val user = userRepository.getUser(userId).getOrThrow()

            // Function to validate the game state and join the game.
            fun joinGame(game: Game): Map<String, Any> {
                if (game.player1.userId == user.userId ||
                    game.player1.status != "online" ||
                    game.player2.userId != "" ||
                    game.gameState != GameState.WAITING_FOR_PLAYER.name) {
                    throw GameError.InvalidGameState()
                }

                // Update the game state and add the second player
                return mapOf(
                    "player2" to user.toDto(),
                    "gameState" to GameState.CHECK_READY.name,
                    "currentPlayer" to listOf(game.player1.userId, userId).random()
                )
            }

            // Update the game state
            gameRepository.updateGameFields(gameId, ::joinGame).getOrThrow()
            // Add the game to the user's games
            userGamesRepository.updateCurrentGameId(userId, gameId).getOrThrow()
            // Register the gameId in the session
            sessionService.setCurrentGameId(gameId)
        }
            .onFailure { e ->
                Timber.e(e, "JoinGameUseCase failed.")
            }
            .recoverCatching { throwable ->
                when (throwable) {
                    is GameError -> throw throwable
                    is UserError -> throw throwable
                    is DataError -> throw throwable
                    else -> throw DomainError.Unknown(throwable)
                }
            }
    }
}