package com.example.seabattle.presentation.screens.leaderboard

import com.example.seabattle.domain.entity.BasicPlayer


data class LeaderboardUiState (
    val user: BasicPlayer? = null,
    val userPosition: Int? = null,
    val usersList: List<BasicPlayer> = emptyList(),
    val errorList: Boolean = false,
    val loadingList: Boolean = true,
    val error: Throwable? = null,
)

