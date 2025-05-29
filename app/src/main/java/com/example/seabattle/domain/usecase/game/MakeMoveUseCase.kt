package com.example.seabattle.domain.usecase.game

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.repository.GameRepository

class MakeMoveUseCase(
    val gameRepository: GameRepository,
    val session: Session,
) {
    suspend operator fun invoke(x: Int, y: Int) : Unit {
        val userId = session.getCurrentUserId()
        val gameId = session.getCurrentGameId()

        if (userId.isEmpty() || gameId.isEmpty()) {
            throw IllegalStateException("User is not logged in or game is not set")
        }

        gameRepository.makeMove(gameId, userId, x, y).getOrThrow()
    }
}