package com.example.seabattle.data.firestore.entities

import com.example.seabattle.domain.model.UserBasic
import com.google.firebase.firestore.FieldValue

data class RoomEntity(
    val roomId: String,
    val roomName: String,
    val roomState: String,
    val numberOfPlayers: Int,
    val player1: UserBasic,
    val player2: UserBasic? = null,
    val gameId: String? = null,
    val createdAt: FieldValue = FieldValue.serverTimestamp()
)

