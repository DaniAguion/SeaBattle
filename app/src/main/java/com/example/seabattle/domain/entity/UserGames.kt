package com.example.seabattle.domain.entity

import com.example.seabattle.data.firestore.dto.BasicPlayerDto
import com.example.seabattle.data.firestore.dto.InvitationDto


data class UserGames(
    val userId: String = "",
    val currentGameId: String? = null,
    val sentGameInvitation: Invitation? = null,
    val gamesInvitations: List<Invitation> = emptyList()
)


data class Invitation(
    val gameId: String = "",
    val invitedTo: BasicPlayer = BasicPlayer(),
    val invitedBy: BasicPlayer = BasicPlayer(),
)