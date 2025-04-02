package com.example.seabattle.ui.login

import androidx.compose.ui.graphics.Color
import com.example.seabattle.R
import com.example.seabattle.validation.ValidationError

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: ValidationError? = null,
    val passwordError: ValidationError? = null,
    val loginResult: LoginMsgs? = null
)

enum class LoginMsgs(val idString: Int, val color: Color) {
    LOGIN_SUCCESSFUL(R.string.login_successful, Color.Green),
    LOGIN_UNSUCCESSFUL(R.string.login_unsuccessful, Color.Red)
}


