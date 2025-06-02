package com.example.seabattle.domain.errors

sealed class UserError(
    val errorMessage: String,
    cause: Throwable? = null
) : Exception(errorMessage, cause) {
    class UserProfileNotFound(cause: Throwable? = null) :
        UserError("User profile could not be found.", cause)
    class NetworkConnection(cause: Throwable? = null) :
        UserError("Network connection problem. Please check your connection.", cause)
    class PermissionDenied(cause: Throwable? = null) :
        UserError("You do not have permission to perform this action.", cause)
    class Unknown(cause: Throwable? = null) :
        UserError("An unexpected error occurred with the user profile. Please try again.", cause)
}