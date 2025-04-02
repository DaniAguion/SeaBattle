package com.example.seabattle.validation

object Validator {

    fun validateEmail(email: String): ValidationError? {
        if (email.isBlank()) return ValidationError.EMPTY_EMAIL
        val emailRegex = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+\$")
        return if (emailRegex.matches(email)) null else ValidationError.INVALID_EMAIL
    }

    fun validatePassword(password: String): ValidationError? {
        if (password.isBlank()) return ValidationError.EMPTY_PASSWORD
        if (password.length < 8) return ValidationError.PASSWORD_TOO_SHORT
        return null
    }

    fun validateNewPassword(password: String): ValidationError? {
        if (validatePassword(password) == null){
            val strongRegex = Regex(".*[A-Z].*[0-9].*")
            return if (strongRegex.matches(password)) null else ValidationError.WEAK_PASSWORD
        }
        return validatePassword(password)
    }
}