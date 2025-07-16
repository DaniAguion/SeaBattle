package com.example.seabattle.data.firestore.dto

data class UserGamesDto(
    val userId: String = "",
    val currentGameId: String? = null,
    val sentGameInvitation: InvitationDto? = null,
    val gameInvitations: List<InvitationDto> = emptyList()
)


data class InvitationDto(
    val gameId: String = "",
    val invitedTo: BasicPlayerDto = BasicPlayerDto(),
    val invitedBy: BasicPlayerDto = BasicPlayerDto(),
)
