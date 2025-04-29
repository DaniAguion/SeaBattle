package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.entities.RoomCreationEntity
import com.example.seabattle.domain.model.Room

fun Room.toCreationEntity(): RoomCreationEntity {
    return RoomCreationEntity(
        roomId = roomId,
        roomName = roomName,
        roomState = roomState,
        numberOfPlayers = numberOfPlayers,
        player1 = player1,
        player2 = player2,
        gameId = gameId
    )
}
