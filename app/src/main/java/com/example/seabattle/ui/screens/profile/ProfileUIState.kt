package com.example.seabattle.ui.screens.profile

import com.example.seabattle.domain.model.UserProfile

data class ProfileUiState(
    val userLoggedIn: Boolean = true,
    val userProfile: UserProfile? = null
)