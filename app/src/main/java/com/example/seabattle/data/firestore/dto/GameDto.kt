package com.example.seabattle.data.firestore.dto

import com.example.seabattle.domain.entity.Ship
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
    val boardForPlayer1: Map<String, Map<String, Int>> = emptyMap(),
    val player1Ships: List<Ship> = emptyList(),
    val player2: UserBasic = UserBasic(),
    val player2Ready: Boolean = false,
    val boardForPlayer2: Map<String, Map<String, Int>> = emptyMap(),
    val player2Ships: List<Ship> = emptyList(),
    val currentTurn: Int = 1,
    val currentPlayer: String = "",
    val gameState: String = "",
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
    var boardForPlayer1: MutableMap<String, MutableMap<String, Int>> = mutableMapOf(),
    val player1Ships: List<Ship> = emptyList(),
    val player2: UserBasic = UserBasic(),
    var player2Ready: Boolean = false,
    var boardForPlayer2: MutableMap<String, MutableMap<String, Int>> = mutableMapOf(),
    val player2Ships: List<Ship> = emptyList(),
    var currentTurn: Int = 1,
    var currentPlayer: String = "",
    var gameState: String = "",
    var gameFinished: Boolean = false,
    var winnerId: Int? = null,
    val createdAt: Date = Date(),
    var updatedAt: Date = Date(),
)

