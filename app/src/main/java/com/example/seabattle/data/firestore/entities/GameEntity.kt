package com.example.seabattle.data.firestore.entities

import com.example.seabattle.domain.model.User
import com.google.firebase.firestore.FieldValue

data class GameEntity(
    val gameId: String,
    val player1: User,
    val player1Board: Map<String, Map<String, Int>>,
    val player2: User,
    val player2Board: Map<String, Map<String, Int>>,
    val currentTurn: Int,
    val currentPlayer: Int,
    val createdAt: FieldValue = FieldValue.serverTimestamp(),
    val updatedAt: FieldValue = FieldValue.serverTimestamp(),
    val gameFinished: Boolean,
    val winnerId: Int? = null,
)