package com.example.seabattle.ui.welcome

import androidx.compose.ui.graphics.Color
import com.example.seabattle.R
import com.example.seabattle.domain.validation.ValidationError

data class WelcomeUiState(
    val email: String = "",
    val password: String = "",
    val emailError: ValidationError? = null,
    val passwordError: ValidationError? = null,
    val msgResult: InfoMsgs? = null,
)

enum class InfoMsgs(val idString: Int, val color: Color) {
    LOGIN_SUCCESSFUL(R.string.login_successful, Color.Green),
    LOGIN_UNSUCCESSFUL(R.string.login_unsuccessful, Color.Red),
    REGISTER_SUCCESSFUL(R.string.register_successful, Color.Green),
    REGISTER_UNSUCCESSFUL(R.string.register_unsuccessful, Color.Red)
}


