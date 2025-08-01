package com.example.seabattle.domain.usecase.game

import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameState

class EnableClaimUseCase() {
    operator fun invoke(userId: String, game: Game): Boolean {
        // First, check if the game is in progress and the current player is not the user
        if (userId.isEmpty()) return false
        if (game.gameState != GameState.IN_PROGRESS.name) return false
        if (game.currentPlayer == userId) return false
        if (game.updatedAt == null) return false

        // If the current player is offline for more than 30 seconds
        // or the game has not been updated for more than 1 minute, claim victory
        val currentPlayerOffline = ((game.currentPlayer == game.player1.userId && game.player1.status != "online") ||
                (game.currentPlayer == game.player2.userId && game.player2.status != "online"))

        val updatedAtTime = game.updatedAt.time
        val lastUpdate = (System.currentTimeMillis() - updatedAtTime)/ 1000

        return (currentPlayerOffline && lastUpdate > 30) || (lastUpdate > 60)
    }
}