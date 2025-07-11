package com.example.seabattle.domain.entity

import java.util.Date

data class UserGames(
    val userId: String = "",
    val currentGameId: String? = null,
    val invitedToGameId: String? = null,
    val history: List<GameHistory> = emptyList()
)

data class GameHistory (
    val gameId: String = "",
    val winnerId: String? = null,
    val player1: User,
    val player2: User,
    val scoreTransacted: Boolean = false,
    val playedAt: Date? = Date()
)

