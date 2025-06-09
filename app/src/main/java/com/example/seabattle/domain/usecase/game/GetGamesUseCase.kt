package com.example.seabattle.domain.usecase.game


import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.GameError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class GetGamesUseCase(
    val gameRepository: GameRepository,
    val session: Session,
) {
    operator fun invoke(): Flow<Result<List<Game>>> {
        val userId = session.getCurrentUserId()

        if (userId == "") {
            return flowOf(Result.failure(UserError.UserProfileNotFound()))
        }

        return gameRepository.fetchGames(userId)
            .onEach { result ->
                result.onFailure { throwable ->
                    Timber.e(throwable, "GetGamesUseCase failed.")
                }
            }
            .catch { throwable ->
                Timber.e(throwable, "GetGamesUseCase failed.")
                when (throwable) {
                    is GameError -> emit(Result.failure(throwable))
                    else -> emit(Result.failure(DomainError.Unknown(throwable)))
                }
            }
        }
}