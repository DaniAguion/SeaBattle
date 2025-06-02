package com.example.seabattle.domain.errors

sealed class DomainError(message: String? = null, cause: Throwable? = null) : Exception(message, cause) {
    // General domain errors
    class Unknown(cause: Throwable? = null) : DomainError("An unexpected error occurred. Try again.", cause)

    // Specific domain errors related to authentication and user management
    class UserNotFound : DomainError("User not found")
    class AuthenticationError(message: String? = null, cause: Throwable? = null) : DomainError(message, cause)
    class InvalidCredentials(cause: Throwable? = null) : DomainError("Invalid credentials. Please verify your email and password.", cause)
    class AccountAlreadyExists(cause: Throwable? = null) : DomainError("An account with this email already exists.", cause)
}