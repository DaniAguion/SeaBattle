package com.example.seabattle.ui.register

import androidx.compose.ui.graphics.Color
import com.example.seabattle.R
import com.example.seabattle.domain.validation.ValidationError

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val emailError: ValidationError? = null,
    val passwordError: ValidationError? = null,
    val registerResult: RegisterMsgs? = null
)

enum class RegisterMsgs(val idString: Int, val color: Color) {
    REGISTER_SUCCESSFUL(R.string.register_successful, Color.Green),
    REGISTER_UNSUCCESSFUL(R.string.register_unsuccessful, Color.Red)
}


