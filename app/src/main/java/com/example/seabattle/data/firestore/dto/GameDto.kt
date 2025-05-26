package com.example.seabattle.data.firestore.dto

import com.example.seabattle.domain.entity.UserBasic
import com.google.firebase.firestore.FieldValue
import java.util.Date


/**
 * Data Transfer Object (DTO) for creating a game.
 * This class is used to transfer data from the client to the server when creating a new game.
 */
data class GameCreationDto(
    val gameId: String = "",
    val player1: UserBasic = UserBasic(),
    val player1Ready: Boolean = false,
    val player1Board: Map<String, Map<String, Int>>? = null,
    val player2: UserBasic = UserBasic(),
    val player2Ready: Boolean = false,
    val player2Board: Map<String, Map<String, Int>>? = null,
    val currentTurn: Int = 0,
    val currentPlayer: String? = null,
    val gameState: String? = null,
    val gameFinished: Boolean = false,
    val winnerId: Int? = null,
    val createdAt: FieldValue = FieldValue.serverTimestamp(),
    val updatedAt: FieldValue = FieldValue.serverTimestamp(),
)

/**
 * Data Transfer Object (DTO) for fetching a game.
 */
data class GameDto(
    val gameId: String = "",
    val player1: UserBasic = UserBasic(),
    var player1Ready: Boolean = false,
    var player1Board: Map<String, Map<String, Int>>? = null,
    val player2: UserBasic = UserBasic(),
    var player2Ready: Boolean = false,
    var player2Board: Map<String, Map<String, Int>>? = null,
    var currentTurn: Int = 0,
    var currentPlayer: String? = null,
    var gameState: String? = null,
    var gameFinished: Boolean = false,
    var winnerId: Int? = null,
    val createdAt: Date? = null,
    var updatedAt: Date? = null,
)

