package com.example.seabattle.presentation.screens.welcome

import androidx.compose.ui.graphics.Color
import com.example.seabattle.R
import com.example.seabattle.presentation.screens.welcome.validation.ValidationError

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

enum class InfoMessages(val idString: Int, val color: Color) {
    LOGIN_SUCCESSFUL(R.string.login_successful, Color.Green),
    LOGIN_UNSUCCESSFUL(R.string.login_unsuccessful, Color.Red),
    REGISTER_SUCCESSFUL(R.string.register_successful, Color.Green),
    REGISTER_UNSUCCESSFUL(R.string.register_unsuccessful, Color.Red)
}


