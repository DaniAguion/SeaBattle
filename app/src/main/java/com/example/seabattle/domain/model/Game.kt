package com.example.seabattle.domain.model

import com.google.firebase.firestore.FieldValue
import java.security.Timestamp

data class Game(
    val gameId: String,
    val player1: UserProfile,
    val player1Board: Map<String, Map<String, Int>>,
    val player2: UserProfile,
    val player2Board: Map<String, Map<String, Int>>,
    val currentTurn: Int,
    val currentPlayer: Int,
    val createdAt: FieldValue,
    val updatedAt: FieldValue,
    val gameFinished: Boolean,
    val winnerId: Int?,
)
