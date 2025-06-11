package com.example.seabattle.presentation.screens.game

import com.example.seabattle.domain.entity.Game

data class GameUiState (
    val game: Game? = null,
    val errorMessage: String? = null,
    val userId: String = "",
    val showClaimDialog: Boolean = false,
)