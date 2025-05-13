package com.example.seabattle.domain.entity

import java.util.Date

data class Room(
    val roomId: String = "",
    val roomName: String = "Unnamed room",
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
    CREATING_GAME,
    GAME_CREATED,
    GAME_STARTED
}
