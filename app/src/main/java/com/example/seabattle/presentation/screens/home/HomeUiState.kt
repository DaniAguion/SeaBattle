package com.example.seabattle.presentation.screens.home

import com.example.seabattle.domain.entity.Room
import com.example.seabattle.presentation.validation.ValidationError

data class HomeUiState (
    val roomName: String = "Testing",
    val roomNameError: ValidationError? = null,
    val roomList: List<Room>,
    val errorList: Boolean = false,
    val loadingList: Boolean = true,
    val hasJoined: Boolean = false,
    val actionFailed: Boolean = false
)

