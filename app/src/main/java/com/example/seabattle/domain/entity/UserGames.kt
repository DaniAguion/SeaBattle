package com.example.seabattle.domain.entity

data class UserGames(
    val userId: String = "",
    val currentGameId: String = "",
    val invitedToGameId: String = "",
    val historial: List<GameHistory> = emptyList()
)

data class GameHistory (
    val gameId: String = "",
    val winnerId: String? = null,
    val player1: User,
    val player2: User,
    val scoreTransacted: Boolean = false,
    val playedAt: String = ""
)

