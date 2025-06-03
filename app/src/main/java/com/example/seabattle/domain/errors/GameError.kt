package com.example.seabattle.domain.errors

sealed class GameError(
    val errorMessage: String,
    cause: Throwable? = null
) : Exception(errorMessage, cause) {
    class GameNotFound(cause: Throwable? = null) :
        GameError("Game could not be found.", cause)
    class GameNotValid(cause: Throwable? = null) :
        GameError("Game data is not valid or corrupted.", cause)
    class InvalidGameState(cause: Throwable? = null) :
        GameError("The game is in an invalid state for this operation.", cause)
    class NetworkConnection(cause: Throwable? = null) :
        GameError("Network connection problem. Please check your connection.", cause)
    class PermissionDenied(cause: Throwable? = null) :
        GameError("You do not have permission to perform this action.", cause)
    class Unknown(cause: Throwable? = null) :
        GameError("An unexpected error occurred with the game. Please try again.", cause)
}
