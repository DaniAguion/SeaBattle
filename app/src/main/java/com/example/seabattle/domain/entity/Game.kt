package com.example.seabattle.domain.entity

import java.util.Date

data class Game(
    val gameId: String,
    val player1: UserBasic,
    val boardForPlayer1: MutableMap<String, MutableMap<String, Int>> = mutableMapOf(),  // Board where the player 1 plays, it contains the ships of player 2.
    val player1Ready: Boolean = false,
    val player1Ships: MutableList<Ship> = mutableListOf(), // List of ships of player 1 and their state.
    val player2: UserBasic,
    val boardForPlayer2: MutableMap<String, MutableMap<String, Int>> = mutableMapOf(),
    val player2Ready: Boolean = false,
    val player2Ships: MutableList<Ship> = mutableListOf(),
    var currentTurn: Int = 0,
    var currentPlayer: String = "",
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
