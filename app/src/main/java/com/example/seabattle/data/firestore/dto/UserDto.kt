package com.example.seabattle.data.firestore.dto

import java.util.Date

data class UserDto(
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val status: String = "offline",
    val score: Int = 1000,
    val history: List<GameHistoryDto> = emptyList(),
)


data class PlayerDto(
    val userId: String = "",
    val displayName: String = "",
    val photoUrl: String = "",
    val status: String = "offline",
    val score: Int = 1000
)


data class BasicPlayerDto(
    val userId: String = "",
    val displayName: String = "",
    val photoUrl: String = "",
    val score: Int = 1000
)


data class GameHistoryDto (
    val gameId: String = "",
    val winnerId: String? = null,
    val player1: BasicPlayerDto = BasicPlayerDto(),
    val player2: BasicPlayerDto = BasicPlayerDto(),
    val scoreTransacted: Long = 0L,
    val playedAt: Date? = Date()
)

