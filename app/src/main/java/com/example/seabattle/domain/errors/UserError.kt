package com.example.seabattle.domain.errors

sealed class UserError(
    val errorMessage: String,
    cause: Throwable? = null
) : Exception(errorMessage, cause) {
    class UserProfileNotFound(cause: Throwable? = null) :
        UserError("User profile could not be found.", cause)
    class UserGamesNotFound(cause: Throwable? = null) :
        UserError("User games could not be found.", cause)
    class InvalidUserGamesData(cause: Throwable? = null) :
        UserError("User games data is not valid or corrupted.", cause)
    class InvalidGuest(cause: Throwable? = null) :
        UserError("Invalid guest user", cause)
}