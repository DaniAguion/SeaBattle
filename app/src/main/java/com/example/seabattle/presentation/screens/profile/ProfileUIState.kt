package com.example.seabattle.presentation.screens.profile

import com.example.seabattle.domain.model.UserProfile

data class ProfileUiState(
    val userLoggedIn: Boolean = true,
    val userProfile: UserProfile = UserProfile()
)