package com.example.seabattle.presentation.screens.home

import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.Invitation
import com.example.seabattle.domain.entity.Player

data class HomeUiState (
    val searchedUser: String = "",
    val invitationsList: List<Invitation> = emptyList(),
    val playersList: List<Player> = emptyList(),
    val searchDone: Boolean = false,
    val loadingPlayersList: Boolean = false,
    val errorPlayersList: Boolean = false,
    val gamesList: List<Game>,
    val loadingGamesList: Boolean = true,
    val errorGameList: Boolean = false,
    val hasJoined: Boolean = false,
    val error: Throwable? = null,
)

