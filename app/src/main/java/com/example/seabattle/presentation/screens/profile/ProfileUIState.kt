package com.example.seabattle.presentation.screens.profile

import com.example.seabattle.domain.model.LocalUser

data class ProfileUiState(
    val userLoggedIn: Boolean = true,
    val user: LocalUser = LocalUser()
)