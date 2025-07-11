package com.example.seabattle.domain.errors

sealed class UserError(
    val errorMessage: String,
    cause: Throwable? = null
) : Exception(errorMessage, cause) {
    class UserProfileNotFound(cause: Throwable? = null) :
        UserError("User profile could not be found.", cause)
}