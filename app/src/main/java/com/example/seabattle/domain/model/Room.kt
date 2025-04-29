package com.example.seabattle.domain.model

import java.util.Date

data class Room(
    val roomId: String = "",
    val roomName: String = "",
    val roomState: String = "",
    val numberOfPlayers: Int = 0,
    val player1: UserBasic = UserBasic(),
    val player2: UserBasic? = null,
    val gameId: String? = null,
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
)

enum class RoomState {
    WAITING_FOR_PLAYER,
    SECOND_PLAYER_JOINED,
    STARTING_GAME
}
