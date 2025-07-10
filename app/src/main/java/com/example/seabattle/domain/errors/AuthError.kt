package com.example.seabattle.domain.errors

sealed class AuthError(
    val errorMessage: String,
    cause: Throwable? = null
) : Exception(errorMessage, cause) {

    class InvalidCredentials(cause: Throwable? = null) :
        AuthError("Invalid credentials. Please verify your email and password.", cause)

    class InvalidUser(cause: Throwable? = null) :
        AuthError("User does not exist or is disabled.", cause)

    class UserCollision(cause: Throwable? = null) :
        AuthError("An account with this email already exists.", cause)

    class RecentLoginRequired(cause: Throwable? = null) :
        AuthError("Recent login required. Please log in again.", cause)

    class NetworkConnection(cause: Throwable? = null) :
        AuthError("Network connection problem. Please check your connection.", cause)

    class Unknown(cause: Throwable? = null) :
        AuthError("An unexpected error occurred. Try again.", cause)
}
