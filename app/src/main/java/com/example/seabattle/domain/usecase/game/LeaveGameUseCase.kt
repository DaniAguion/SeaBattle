package com.example.seabattle.domain.usecase.game

import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.errors.DataError
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.GameError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.GameRepository
import com.example.seabattle.domain.repository.UserGamesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber


class LeaveGameUseCase(
    val gameRepository: GameRepository,
    val userGamesRepository: UserGamesRepository,
    val sessionService: SessionService,
    val ioDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(userId:String, game: Game?): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            if (userId.isEmpty()) {
                throw UserError.UserProfileNotFound()
            }

            if (game ==null || game.gameId.isEmpty()) {
                throw GameError.GameNotFound()
            }

            // If the games is not aborted or finished, it is necessary to update the game state
            if (game.gameState != GameState.GAME_ABORTED.name && game.gameState != GameState.GAME_FINISHED.name) {

                fun leaveGame(game: Game): Map<String, Any> {
                    if (game.gameState == GameState.WAITING_FOR_PLAYER.name || game.gameState == GameState.CHECK_READY.name) {
                        return mapOf(
                            "gameState" to GameState.GAME_ABORTED.name,
                        )
                    } else if (game.gameState == GameState.IN_PROGRESS.name) {
                        val winnerId = if (game.player1.userId == userId) game.player2.userId else game.player1.userId
                        return mapOf(
                            "gameState" to GameState.GAME_FINISHED.name,
                            "winnerId" to winnerId
                        )
                    } else {
                        throw GameError.InvalidGameState()
                    }
                }
                gameRepository.updateGameFields(gameId = game.gameId, logicFunction = ::leaveGame).getOrThrow()

            }
            userGamesRepository.updateCurrentGameId(userId = userId, gameId = null).getOrThrow()
            sessionService.clearCurrentGame()
        }
        .onFailure { e ->
            Timber.e(e, "LeaveGameUseCase failed.")
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