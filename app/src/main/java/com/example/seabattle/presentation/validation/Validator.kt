package com.example.seabattle.presentation.validation

object Validator {

    fun validateUsername(username: String): ValidationError? {
        if (username.isBlank()) return ValidationError.EmptyUsername
        val usernameRegex = Regex("^[a-zA-Z0-9_]{3,15}\$")
        return if (usernameRegex.matches(username)) null else ValidationError.InvalidUsername
    }

    fun validateEmail(email: String): ValidationError? {
        if (email.isBlank()) return ValidationError.EmptyEmail
        val emailRegex = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+\$")
        return if (emailRegex.matches(email)) null else ValidationError.InvalidEmail
    }

    fun validatePassword(password: String): ValidationError? {
        if (password.isBlank()) return ValidationError.EmptyPassword
        if (password.length < 8) return ValidationError.PasswordShort
        return null
    }

    fun validateNewPassword(password: String): ValidationError? {
        if (validatePassword(password) == null){
            val strongRegex = Regex(".*[A-Z].*[0-9].*")
            return if (strongRegex.matches(password)) null else ValidationError.PasswordWeak
        }
        return validatePassword(password)
    }

    fun validateRoomName(roomName: String): ValidationError? {
        if (roomName.isBlank()) return ValidationError.EmptyRoomName
        if (roomName.length < 4) return ValidationError.RoomNameShort
        return null
    }
}