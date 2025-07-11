package com.example.seabattle.data.firestore.dto

import java.util.Date

data class GameHistoryDto (
    val gameId: String = "",
    val winnerId: String? = null,
    val player1: UserDto,
    val player2: UserDto,
    val scoreTransacted: Boolean = false,
    val playedAt: Date? = Date()
)

