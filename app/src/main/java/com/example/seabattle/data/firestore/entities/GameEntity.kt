package com.example.seabattle.data.firestore.entities

import com.example.seabattle.domain.model.UserBasic
import com.google.firebase.firestore.FieldValue

data class GameEntity(
    val gameId: String,
    val player1: UserBasic,
    val player1Board: Map<String, Map<String, Int>>,
    val player2: UserBasic,
    val player2Board: Map<String, Map<String, Int>>,
    val currentTurn: Int,
    val currentPlayer: Int,
    val createdAt: FieldValue = FieldValue.serverTimestamp(),
    val updatedAt: FieldValue = FieldValue.serverTimestamp(),
    val gameFinished: Boolean,
    val winnerId: Int? = null,
)

