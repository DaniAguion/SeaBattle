package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.dto.RoomDtoRd
import com.example.seabattle.data.firestore.dto.RoomDtoWr
import com.example.seabattle.domain.entity.Room


// This function maps a Room object to a create a new Room in server.
// The createdAt and updatedAt fields are initialized as server timestamps.
fun Room.toRoomDto(): RoomDtoWr {
    return RoomDtoWr(
        roomId = roomId,
        roomName = roomName,
        roomState = roomState,
        numberOfPlayers = numberOfPlayers,
        player1 = player1,
        player2 = player2,
        gameId = gameId,
    )
}

// This function is used to read a Room object from the server.
fun RoomDtoRd.toRoomEntity(): Room {
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

