package com.example.seabattle.domain.errors

sealed class DomainError(message: String? = null, cause: Throwable? = null) : Exception(message, cause) {
    class Unknown(cause: Throwable? = null) : DomainError("An unexpected error occurred. Try again.", cause)
    class PresenceError(cause: Throwable? = null) : DomainError("Unable to establish connection.", cause)
}