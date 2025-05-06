package com.example.seabattle.data.firestore.dto

import com.example.seabattle.domain.entity.UserBasic
import com.google.firebase.firestore.FieldValue
import java.util.Date

data class GameDtoWr(
    val gameId: String,
    val player1: UserBasic,
    val player1Board: Map<String, Map<String, Int>>? = null,
    val player2: UserBasic? = null,
    val player2Board: Map<String, Map<String, Int>>? = null,
    val gameState: String? = null,
    val currentTurn: Int,
    val currentPlayer: String? = null,
    val gameFinished: Boolean,
    val winnerId: Int? = null,
    val createdAt: FieldValue = FieldValue.serverTimestamp(),
    val updatedAt: FieldValue = FieldValue.serverTimestamp(),
)


data class GameDtoRd(
    val gameId: String,
    val player1: UserBasic,
    val player1Board: Map<String, Map<String, Int>>? = null,
    val player2: UserBasic? = null,
    val player2Board: Map<String, Map<String, Int>>? = null,
    val currentTurn: Int,
    val currentPlayer: String? = null,
    val gameState: String? = null,
    val gameFinished: Boolean = false,
    val winnerId: Int? = null,
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
)

