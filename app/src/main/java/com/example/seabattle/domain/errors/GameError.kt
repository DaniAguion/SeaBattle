package com.example.seabattle.domain.errors

sealed class GameError(
    val errorMessage: String,
    cause: Throwable? = null
) : Exception(errorMessage, cause) {
    class GameNotFound(cause: Throwable? = null) :
        GameError("Game could not be found.", cause)
    class InvalidData(cause: Throwable? = null) :
        GameError("Game data is not valid or corrupted.", cause)
    class InvalidGameState(cause: Throwable? = null) :
        GameError("The game is in an invalid state for this operation.", cause)
}
