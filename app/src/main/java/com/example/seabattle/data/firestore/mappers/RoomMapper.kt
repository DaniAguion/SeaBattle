package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.dto.RoomCreationDTO
import com.example.seabattle.domain.entity.Room

fun Room.toCreationDTO(): RoomCreationDTO {
    return RoomCreationDTO(
        roomId = roomId,
        roomName = roomName,
        roomState = roomState,
        numberOfPlayers = numberOfPlayers,
        player1 = player1,
        player2 = player2,
        gameId = gameId
    )
}
