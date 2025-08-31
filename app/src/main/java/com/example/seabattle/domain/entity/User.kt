package com.example.seabattle.domain.entity

import java.time.Instant
import java.time.Instant.now


// Represents the user profile in the application
data class User(
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val status: String = "offline",
    val score: Int = 1000,
    val history: List<GameHistory> = emptyList()
)


// Represents a player in the game with essential attributes
data class Player (
    val userId: String = "",
    val displayName: String = "",
    val photoUrl: String = "",
    val status: String = "offline",
    val score: Int = 1000
)

// Represents a basic player entity with minimal information
data class BasicPlayer(
    val userId: String = "",
    val displayName: String = "",
    val photoUrl: String = "",
    val score: Int = 1000
)

data class GameHistory (
    val gameId: String = "",
    val winnerId: String? = null,
    val player1: BasicPlayer,
    val player2: BasicPlayer,
    val scoreTransacted: Long = 0L,
    val playedAt: Instant? = null
)