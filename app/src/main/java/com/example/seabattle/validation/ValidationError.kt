package com.example.seabattle.validation

import androidx.compose.ui.graphics.Color
import com.example.seabattle.R

enum class ValidationError(val idString: Int) {
    EMPTY_EMAIL(R.string.empty_email),
    EMPTY_PASSWORD(R.string.empty_password),
    INVALID_EMAIL(R.string.error_invalid_email),
    PASSWORD_TOO_SHORT(R.string.error_short_password),
    WEAK_PASSWORD(R.string.weak_password),
}