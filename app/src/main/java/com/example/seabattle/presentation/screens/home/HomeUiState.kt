package com.example.seabattle.presentation.screens.home

import com.example.seabattle.domain.entity.Game
import com.example.seabattle.presentation.resources.ValidationError

data class HomeUiState (
    val gamesList: List<Game>,
    val errorList: Boolean = false,
    val loadingList: Boolean = true,
    val hasJoined: Boolean = false,
    val error: Throwable? = null,
)

