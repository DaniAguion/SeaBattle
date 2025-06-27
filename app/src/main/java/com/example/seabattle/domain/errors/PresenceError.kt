package com.example.seabattle.domain.errors

sealed class PresenceError(
    val errorMessage: String,
    cause: Throwable? = null
) : Exception(errorMessage, cause) {
    class UserNotAuthenticated(cause: Throwable? = null) :
        PresenceError("User is not authenticated. Please log in to set presence.", cause)

    class InvalidStatusValue(cause: Throwable? = null) :
        PresenceError("Received an invalid status value from the database (expected 'online' or 'offline').", cause)

    class NetworkConnection(cause: Throwable? = null) :
        PresenceError("Network connection problem during presence operation. Please check your connection.", cause)

    class Unknown(cause: Throwable? = null) :
        PresenceError("An unexpected error occurred with user presence. Please try again.", cause)
}