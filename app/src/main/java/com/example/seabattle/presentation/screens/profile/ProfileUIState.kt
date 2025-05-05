package com.example.seabattle.presentation.screens.profile

import com.example.seabattle.domain.entity.UserLocal

data class ProfileUiState(
    val userLoggedIn: Boolean = true,
    val user: UserLocal = UserLocal()
)