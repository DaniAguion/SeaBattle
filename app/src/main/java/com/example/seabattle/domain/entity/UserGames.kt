package com.example.seabattle.domain.entity

import com.example.seabattle.data.firestore.dto.BasicUserDto
import com.example.seabattle.data.firestore.dto.InvitationDto
import java.util.Date

data class UserGames(
    val userId: String = "",
    val currentGameId: String? = null,
    val invitedToGameId: List<Invitation> = emptyList(),
    val history: List<GameHistory> = emptyList()
)


data class Invitation(
    val gameId: String = "",
    val gameName: String = "",
    val invitationState: String = "",
    val invitedBy: User = User(),
)


enum class InvitationState {
    ACCEPTED, DECLINED, PENDING
}


data class GameHistory (
    val gameId: String = "",
    val winnerId: String? = null,
    val player1: User,
    val player2: User,
    val scoreTransacted: Long = 0L,
    val playedAt: Date? = Date()
)

