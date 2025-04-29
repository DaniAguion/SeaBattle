package com.example.seabattle.presentation.screens.home

import com.example.seabattle.R
import com.example.seabattle.domain.model.Room

data class HomeUiState (
    val roomList: List<Room>,
    val errorList: Boolean = false,
)

