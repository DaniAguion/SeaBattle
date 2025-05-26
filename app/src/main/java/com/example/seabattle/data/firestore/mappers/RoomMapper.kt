package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.dto.RoomDto
import com.example.seabattle.domain.entity.Room


// Function to convert Room DTO to Room Entity
fun RoomDto.toRoomEntity(): Room {
    return Room(
        roomId = roomId,
        roomName = roomName,
        roomState = roomState,
        numberOfPlayers = numberOfPlayers,
        player1 = player1,
        player2 = player2,
        gameId = gameId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

