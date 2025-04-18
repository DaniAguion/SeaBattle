package com.example.seabattle.domain.model

data class Game(
    val id: String,
    val player1: UserProfile,
    val player1BattlePlan: List<List<Int>>,
    val player1Board: List<List<Int>>,
    val player2: UserProfile,
    val player2BattlePlan: List<List<Int>>,
    val player2Board: List<List<Int>>,
    val actualTurn: Int,
    val actualPlayer: Int,
    val turnTimer: Long,
    val gameStatus: Int,
    val winner: Int?,
)
