package com.example.seabattle.domain.entity

import java.util.Date

data class Game(
    val gameId: String,
    val player1: UserBasic,
    val player1Board: Map<String, Map<String, Int>> = emptyMap(),
    val player1Ready: Boolean = false,
    val player2: UserBasic,
    val player2Board: Map<String, Map<String, Int>> = emptyMap(),
    val player2Ready: Boolean = false,
    val currentTurn: Int = 0,
    val currentPlayer: String = "",
    val gameState: String = "",
    val gameFinished: Boolean = false,
    val winnerId: Int? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)


// This enum represents the possible states of a game.
// The timeout and game abandoned states are used to handle cases where a player does not respond in time or leaves the game.
// This cases are controlled by the server through cloud functions.
enum class GameState {
    CHECK_READY,
    GAME_ABORTED,
    IN_PROGRESS,
    PLAYER_1_WON,
    PLAYER_2_WON,
    PLAYER_1_TIMEOUT,
    PLAYER_2_TIMEOUT,
    GAME_ABANDONED
}
