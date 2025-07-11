package com.example.seabattle.data.firestore.dto


data class UserGamesDto(
    val userId: String = "",
    val currentGameId: String? = null,
    val invitedToGameId: String? = null,
    val history: List<GameHistoryDto> = emptyList(),
)
