package com.example.seabattle.presentation.screens.profile

import com.example.seabattle.domain.entity.User

data class ProfileUiState(
    val userLoggedIn: Boolean = true,
    val user: User = User(),
    val errorList: Boolean = false,
    val loadingList: Boolean = true,
    val error: Throwable? = null,
    val deleteDialog: Boolean = false,
)