package com.example.seabattle.data.firestore.entities

import com.example.seabattle.domain.model.UserBasic
import com.google.firebase.firestore.FieldValue

data class RoomCreationEntity(
    val roomId: String = "",
    val roomName: String = "",
    val roomState: String = "",
    val numberOfPlayers: Int = 0,
    val player1: UserBasic = UserBasic(),
    val player2: UserBasic? = null,
    val gameId: String? = null,
    val createdAt: FieldValue = FieldValue.serverTimestamp(),
    val updatedAt: FieldValue = FieldValue.serverTimestamp(),
)

