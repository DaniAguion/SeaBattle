package com.example.seabattle.domain.usecase.game

import com.example.seabattle.domain.CheckerService
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
                if (CheckerService.claimVictoryConditions(userId = userId, game = game)) {
                    return mapOf(
                        "gameState" to GameState.GAME_FINISHED.name,
                        "winnerId" to userId,
                    )
                } else throw GameError.InvalidGameState()
            }

            gameRepository.updateGameFields(gameId = gameId, logicFunction = ::checkUserAFK).getOrThrow()
        }
        .onFailure { e ->
            Timber.e(e, "ClaimVictoryUseCase failed.")
        }
        .recoverCatching { throwable ->
            if (throwable is GameError) throw throwable
            else if (throwable is UserError) throw throwable
            else throw DomainError.Unknown(throwable)
        }
    }
}