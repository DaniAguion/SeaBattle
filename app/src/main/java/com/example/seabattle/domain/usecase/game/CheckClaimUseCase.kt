package com.example.seabattle.domain.usecase.game

import com.example.seabattle.domain.CheckerService
import com.example.seabattle.domain.Session
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.GameError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.GameRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber


class CheckClaimUseCase(
    val gameRepository: GameRepository,
    val session: Session,
    val ioDispatcher: CoroutineDispatcher,
) {

    suspend operator fun invoke(): Result<Boolean> = withContext(ioDispatcher) {
        runCatching {
            val userId = session.getCurrentUserId()
            val gameId = session.getCurrentGameId()

            if (userId.isEmpty()) {
                throw UserError.UserProfileNotFound()
            }

            if (gameId.isEmpty()) {
                throw GameError.GameNotFound()
            }

            val game = gameRepository.getGame(gameId).getOrThrow()
            CheckerService.claimVictoryConditions(userId = userId, game = game)
        }
        .onFailure { e ->
            Timber.e(e, "CheckClaimUseCase failed.")
        }
        .recoverCatching { throwable ->
            if (throwable is GameError) throw throwable
            else if (throwable is UserError) throw throwable
            else throw DomainError.Unknown(throwable)
        }
    }
}