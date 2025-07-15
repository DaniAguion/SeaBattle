package com.example.seabattle.data.firestore.dto

data class UserGamesDto(
    val userId: String = "",
    val currentGameId: String? = null,
    val gameInvitations: List<InvitationDto> = emptyList()
)


data class InvitationDto(
    val gameId: String = "",
    val gameName: String = "",
    val invitedBy: BasicPlayerDto = BasicPlayerDto(),
)
