package com.example.seabattle.presentation.screens.home

import com.example.seabattle.domain.entity.Room

data class HomeUiState (
    val roomList: List<Room>,
    val errorList: Boolean = false,
    val loadingList: Boolean = true
)

