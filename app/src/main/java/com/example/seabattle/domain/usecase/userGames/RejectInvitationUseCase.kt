package com.example.seabattle.domain.usecase.userGames

import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.entity.Invitation
import com.example.seabattle.domain.errors.DataError
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.GameError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.GameRepository
import com.example.seabattle.domain.repository.UserGamesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber


class RejectInvitationUseCase(
    val gameRepository: GameRepository,
    val userGamesRepository: UserGamesRepository,
    val ioDispatcher: CoroutineDispatcher,
    val sessionService: SessionService,
) {
    suspend operator fun invoke(invitation: Invitation): Result<Unit>
    = withContext(ioDispatcher) {
        runCatching {
            val userId = sessionService.getCurrentUserId()
            if (userId.isEmpty()) throw UserError.UserProfileNotFound()

            val gameId = invitation.gameId
            if (gameId.isEmpty()) throw GameError.GameNotFound()

            // Delete the invitation from both players data
            userGamesRepository.cancelInvitation(userId).getOrThrow()

            val game = gameRepository.getGame(gameId).getOrThrow()

            // If the game is in the waiting state, we can cancel it
            if (game.gameState == GameState.WAITING_FOR_PLAYER.name) {
                fun cancelGame(game: Game): Map<String, Any> {
                    // Check if the game is in the waiting state just before canceling
                    if (game.gameState == GameState.WAITING_FOR_PLAYER.name) {
                        return mapOf(
                            "gameState" to GameState.GAME_ABORTED.name,
                        )
                    } else {
                        throw GameError.InvalidGameState()
                    }
                }
                gameRepository.updateGameFields(gameId = game.gameId, logicFunction = ::cancelGame).getOrThrow()
            }
        }
        .onFailure { e ->
            Timber.e(e, "RejectInvitationUseCase failed.")
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