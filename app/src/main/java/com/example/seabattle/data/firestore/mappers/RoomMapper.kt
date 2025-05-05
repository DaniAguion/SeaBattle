package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.dto.RoomCreationDTO
import com.example.seabattle.domain.entity.Room
import com.google.firebase.firestore.FieldValue

// This function maps a Room object to a creationDTO object.
fun Room.toCreationDTO(): RoomCreationDTO {
    return RoomCreationDTO(
        roomId = roomId,
        roomName = roomName,
        roomState = roomState,
        numberOfPlayers = numberOfPlayers,
        player1 = player1,
        player2 = player2,
        gameId = gameId,
        createdAt = FieldValue.serverTimestamp(),
        updatedAt = FieldValue.serverTimestamp()
    )
}
