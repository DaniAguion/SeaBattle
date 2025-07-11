package com.example.seabattle.presentation.screens.profile

import com.example.seabattle.domain.entity.User

data class ProfileUiState(
    val userLoggedIn: Boolean = true,
    val user: User = User(),
    val error: Throwable? = null,
    val deleteDialog: Boolean = false
)