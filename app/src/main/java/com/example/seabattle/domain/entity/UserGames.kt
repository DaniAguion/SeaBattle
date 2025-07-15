package com.example.seabattle.domain.entity


data class UserGames(
    val userId: String = "",
    val currentGameId: String? = null,
    val gamesInvitations: List<Invitation> = emptyList()
)


data class Invitation(
    val gameId: String = "",
    val gameName: String = "",
    val invitedBy: BasicPlayer = BasicPlayer(),
)