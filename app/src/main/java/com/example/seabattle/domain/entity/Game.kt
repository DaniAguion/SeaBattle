package com.example.seabattle.domain.entity

data class Game(
    val gameId: String,
    val gameName: String,
    val numberOfPlayers: Int = 1,
    val player1: UserBasic,
    val boardForPlayer1: Map<String, Map<String, Int>> = emptyMap(),  // Board where the player 1 plays, it contains the ships of player 2.
    val player1Ready: Boolean = false,
    val player1Ships: List<Ship> = emptyList(), // List of ships of player 1 and their state.
    val player2: UserBasic,
    val boardForPlayer2: Map<String, Map<String, Int>> = emptyMap(),
    val player2Ready: Boolean = false,
    val player2Ships: List<Ship> = emptyList(),
    val currentTurn: Int = 0,
    val currentPlayer: String = "",
    val gameState: String = "",
    val winnerId: String? = null
)


// This enum represents the possible states of a game.
// The timeout and game abandoned states are used to handle cases where a player does not respond in time or leaves the game.
// This cases are controlled by the server through cloud functions.
enum class GameState {
    WAITING_FOR_PLAYER,
    CHECK_READY,
    GAME_ABORTED,
    IN_PROGRESS,
    USER_LEFT,
    GAME_FINISHED,
    GAME_ABANDONED // This state is set by the server when there is no changes in the game for a long time.
}


