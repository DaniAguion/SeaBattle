package com.example.seabattle.presentation.screens.room

import com.example.seabattle.domain.entity.Room

data class RoomUiState(
    val room: Room? = null,
    val actionFailed: Boolean = false
)