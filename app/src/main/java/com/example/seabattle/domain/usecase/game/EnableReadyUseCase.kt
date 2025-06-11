package com.example.seabattle.domain.usecase.game

import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameState

class EnableReadyUseCase() {
    operator fun invoke(userId: String, game: Game): Boolean {
        if (userId.isEmpty()) return false
        val conditionsMet = ((game.gameState == GameState.CHECK_READY.name) &&
                ((userId == game.player1.userId && game.player1Ready == false) ||
                (userId == game.player2.userId && game.player2Ready == false)))

        return conditionsMet
    }
}
