package com.example.seabattle.domain.model

data class Game(
    val gameId: String,
    val player1: UserBasic,
    val player1Board: Map<String, Map<String, Int>>,
    val player2: UserBasic,
    val player2Board: Map<String, Map<String, Int>>,
    val currentTurn: Int,
    val currentPlayer: Int,
    val gameFinished: Boolean,
    val winnerId: Int?,
)
