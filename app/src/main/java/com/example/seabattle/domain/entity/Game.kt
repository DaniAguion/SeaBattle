package com.example.seabattle.domain.entity

import java.util.Date

data class Game(
    val gameId: String,
    val privateGame: Boolean = false,
    val player1: Player,
    val boardForPlayer1: Map<String, Map<String, Int>> = emptyMap(),  // Board where the player 1 plays, it contains the ships of player 2.
    val player1Ready: Boolean = false,
    val player1Ships: List<Ship> = emptyList(), // List of ships of player 1 and their state.
    val player2: Player,
    val boardForPlayer2: Map<String, Map<String, Int>> = emptyMap(),
    val player2Ready: Boolean = false,
    val player2Ships: List<Ship> = emptyList(),
    val currentPlayer: String = "",
    val gameState: String = "",
    val winnerId: String? = null,
    val scoreTransacted: Boolean = false,
    val createdAt: Date? = Date(),
    val expireAt: Date? = Date(),
    val updatedAt: Date? = Date(),
)


// This enum represents the possible states of a game.
enum class GameState {
    WAITING_FOR_PLAYER,
    CHECK_READY,
    GAME_ABORTED,
    IN_PROGRESS,
    GAME_FINISHED
}


