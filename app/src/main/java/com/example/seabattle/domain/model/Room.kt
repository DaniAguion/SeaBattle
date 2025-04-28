package com.example.seabattle.domain.model

data class Room(
    val roomId: String,
    val roomName: String,
    val roomState: String,
    val numberOfPlayers: Int,
    val player1: UserBasic,
    val player2: UserBasic? = null,
    val gameId: String? = null
)

enum class RoomState {
    WAITING_FOR_PLAYER,
    SECOND_PLAYER_JOINED,
    STARTING_GAME
}
