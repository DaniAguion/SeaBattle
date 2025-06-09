package com.example.seabattle.presentation.validation

import com.example.seabattle.R

sealed class ValidationError(val idString: Int) {
    object EmptyUsername : ValidationError(R.string.error_empty_username)
    object InvalidUsername : ValidationError(R.string.error_invalid_username)
    object EmptyEmail : ValidationError(R.string.error_empty_email)
    object InvalidEmail : ValidationError(R.string.error_invalid_email)
    object EmptyPassword : ValidationError(R.string.error_empty_password)
    object PasswordShort : ValidationError(R.string.error_short_password)
    object PasswordWeak : ValidationError(R.string.weak_password)
    object EmptyGameName : ValidationError(R.string.error_empty_gameName)
    object GameNameShort : ValidationError(R.string.error_invalid_gameName)
}