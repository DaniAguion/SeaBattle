package com.example.seabattle.domain.model

data class Game(
    val gameId: String,
    val player1: UserBasic,
    val player1Board: Map<String, Map<String, Int>>? = null,
    val player2: UserBasic? = null,
    val player2Board: Map<String, Map<String, Int>>? = null,
    val currentTurn: Int,
    val currentPlayer: String? = null,
    val gameState: String? = null,
    val gameFinished: Boolean = false,
    val winnerId: Int? = null,
)

enum class GameState {
    WAITING_FOR_PLAYER,
    CHECK_READY,
    IN_PROGRESS,
    PLAYER_1_WON,
    PLAYER_2_WON,
    DRAW,
}

enum class Player {
    PLAYER_1,
    PLAYER_2,
}