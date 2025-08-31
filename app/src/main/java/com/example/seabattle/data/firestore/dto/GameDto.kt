package com.example.seabattle.data.firestore.dto

import com.example.seabattle.domain.entity.Ship
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue


/**
 * Data Transfer Object (DTO) for creating a game.
 * This class is used to transfer data from the client to the server when creating a new game.
 */
data class GameCreationDto(
    val gameId: String = "",
    val privateGame: Boolean = false,
    val player1: PlayerDto = PlayerDto(),
    val player1Ready: Boolean = false,
    val boardForPlayer1: Map<String, Map<String, Int>> = emptyMap(),
    val player1Ships: List<Ship> = emptyList(),
    val player2: PlayerDto = PlayerDto(),
    val player2Ready: Boolean = false,
    val boardForPlayer2: Map<String, Map<String, Int>> = emptyMap(),
    val player2Ships: List<Ship> = emptyList(),
    val currentPlayer: String = "",
    val gameState: String = "",
    val winnerId: String? = null,
    val scoreTransacted: Boolean = false,
    val createdAt: FieldValue = FieldValue.serverTimestamp(),
    val expireAt: FieldValue = FieldValue.serverTimestamp(),
    val updatedAt: FieldValue = FieldValue.serverTimestamp(),
)

/**
 * Data Transfer Object (DTO) for fetching a game.
 */
data class GameDto(
    val gameId: String = "",
    val gameName: String = "",
    val privateGame: Boolean = false,
    val player1: PlayerDto = PlayerDto(),
    var player1Ready: Boolean = false,
    var boardForPlayer1: Map<String, Map<String, Int>> = emptyMap(),
    val player1Ships: List<Ship> = emptyList(),
    val player2: PlayerDto = PlayerDto(),
    var player2Ready: Boolean = false,
    var boardForPlayer2: Map<String, Map<String, Int>> = emptyMap(),
    val player2Ships: List<Ship> = emptyList(),
    var currentMove: Int = 1,
    var currentPlayer: String = "",
    var gameState: String = "",
    var winnerId: String? = null,
    var scoreTransacted: Boolean = false,
    val createdAt: Timestamp? = null,
    var updatedAt: Timestamp? = null,
    var expireAt: Timestamp? = null,
)

