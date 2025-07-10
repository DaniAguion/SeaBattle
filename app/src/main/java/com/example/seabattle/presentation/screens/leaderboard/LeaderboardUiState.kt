package com.example.seabattle.presentation.screens.leaderboard

import com.example.seabattle.domain.entity.User


data class LeaderboardUiState (
    val user: User? = null,
    val userPosition: Int? = null,
    val usersList: List<User>,
    val errorList: Boolean = false,
    val loadingList: Boolean = true,
    val error: Throwable? = null,
)

