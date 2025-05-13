package com.example.seabattle.presentation.screens.welcome

import com.example.seabattle.presentation.validation.ValidationError

data class WelcomeUiState(
    val isLoggedIn: Boolean = false,
    val email: String = "",
    val password: String = "",
    val username: String = "",
    val usernameError: ValidationError? = null,
    val emailError: ValidationError? = null,
    val passwordError: ValidationError? = null,
    val msgResult: InfoMessages? = null,
)

enum class InfoMessages {
    LOGIN_SUCCESSFUL,
    LOGIN_UNSUCCESSFUL,
    REGISTER_SUCCESSFUL,
    REGISTER_UNSUCCESSFUL,
}


