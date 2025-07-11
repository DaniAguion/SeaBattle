package com.example.seabattle.data.firestore.dto


data class UserGamesDto(
    val userId: String = "",
    val currentGameId: String = "",
    val invitedToGameId: String = "",
    val historial: List<GameHistoryDto> = emptyList(),
)
