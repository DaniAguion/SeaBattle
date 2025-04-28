package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.entities.RoomEntity
import com.example.seabattle.domain.model.Room

fun RoomEntity.toDomainModel(): Room {
    return Room(
        roomId = roomId,
        roomName = roomName,
        roomState = roomState,
        numberOfPlayers = numberOfPlayers,
        player1 = player1,
        player2 = player2,
        gameId = gameId
    )
}

fun Room.toEntity(): RoomEntity {
    return RoomEntity(
        roomId = roomId,
        roomName = roomName,
        roomState = roomState,
        numberOfPlayers = numberOfPlayers,
        player1 = player1,
        player2 = player2,
        gameId = gameId
    )
}
