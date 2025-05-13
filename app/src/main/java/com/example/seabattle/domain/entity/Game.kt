package com.example.seabattle.domain.entity

import java.util.Date

data class Game(
    val gameId: String,
    val player1: UserBasic,
    val player1Board: Map<String, Map<String, Int>>? = null,
    val player1Ready: Boolean = false,
    val player2: UserBasic,
    val player2Board: Map<String, Map<String, Int>>? = null,
    val player2Ready: Boolean = false,
    val currentTurn: Int = 0,
    val currentPlayer: String? = null,
    val gameState: String? = null,
    val gameFinished: Boolean = false,
    val winnerId: Int? = null,
    val createdAt: Date? = null,
    val updatedAt: Date? = null
)

enum class GameState {
    CHECK_READY,
    IN_PROGRESS,
    PLAYER_1_WON,
    PLAYER_2_WON,
    PLAYER_1_DISCONNECTED,
    PLAYER_2_DISCONNECTED
}
