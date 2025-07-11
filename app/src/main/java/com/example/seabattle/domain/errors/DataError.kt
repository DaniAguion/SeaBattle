package com.example.seabattle.domain.errors

sealed class DataError(message: String? = null, cause: Throwable? = null)
    : Exception(message, cause) {
        class NotFound(cause: Throwable? = null) :
            DataError("Data could not be found.", cause)
        class NetworkConnection(cause: Throwable? = null) :
            DataError("Network connection problem. Please check your connection.", cause)
        class PermissionDenied(cause: Throwable? = null) :
            DataError("You do not have permission to perform this action.", cause)
        class Unknown(cause: Throwable? = null) :
            DataError("An unexpected error occurred with the game. Please try again.", cause)
    }