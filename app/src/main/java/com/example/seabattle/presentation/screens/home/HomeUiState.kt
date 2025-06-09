package com.example.seabattle.presentation.screens.home

import com.example.seabattle.domain.entity.Game
import com.example.seabattle.presentation.validation.ValidationError

data class HomeUiState (
    val gameName: String = "Testing",
    val gameNameError: ValidationError? = null,
    val gamesList: List<Game>,
    val errorList: Boolean = false,
    val loadingList: Boolean = true,
    val hasJoined: Boolean = false,
    val errorMessage: String? = null,
)

