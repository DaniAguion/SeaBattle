package com.example.seabattle.domain.validation

import com.example.seabattle.R

sealed class ValidationError(val idString: Int) {
    object EmptyEmail : ValidationError(R.string.error_empty_email)
    object InvalidEmail : ValidationError(R.string.error_invalid_email)
    object EmptyPassword : ValidationError(R.string.error_empty_password)
    object PasswordShort : ValidationError(R.string.error_short_password)
    object PasswordWeak : ValidationError(R.string.weak_password)
}