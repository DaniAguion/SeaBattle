package com.example.seabattle.data.firestore.dto

import java.util.Date

data class GameHistoryDto (
    val gameId: String = "",
    val winnerId: String? = null,
    val player1: BasicUserDto = BasicUserDto(),
    val player2: BasicUserDto = BasicUserDto(),
    val scoreTransacted: Long = 0L,
    val playedAt: Date? = Date()
)

data class BasicUserDto(
    val userId: String = "",
    val displayName: String = "",
    val photoUrl: String = "",
    val score: Int = 1000
)