package com.example.seabattle.data.firestore.dto

import com.example.seabattle.domain.entity.UserBasic
import com.google.firebase.firestore.FieldValue
import java.util.Date


/**
 * Data Transfer Object (DTO) for creating a room.
 * This class is used to transfer data from the client to the server when creating a new room.
 */
data class RoomCreationDto(
    val roomId: String = "",
    val roomName: String = "",
    val roomState: String = "",
    val numberOfPlayers: Int = 1,
    val player1: UserBasic = UserBasic(),
    val player2: UserBasic? = null,
    val gameId: String? = null,
    val createdAt: FieldValue = FieldValue.serverTimestamp(),
    val updatedAt: FieldValue = FieldValue.serverTimestamp(),
)

/**
 * Data Transfer Object (DTO) for fetching a room.
 */
data class RoomDto(
    val roomId: String = "",
    val roomName: String = "",
    val roomState: String = "",
    val numberOfPlayers: Int = 1,
    val player1: UserBasic = UserBasic(),
    val player2: UserBasic? = null,
    val gameId: String? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
)

