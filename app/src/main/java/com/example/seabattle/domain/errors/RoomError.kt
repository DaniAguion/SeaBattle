package com.example.seabattle.domain.errors

sealed class RoomError(
    val errorMessage: String,
    cause: Throwable? = null
) : Exception(errorMessage, cause) {
    class RoomNotFound(cause: Throwable? = null) :
        RoomError("Room could not be found.", cause)
    class RoomNotValid(cause: Throwable? = null) :
        RoomError("Room data is not valid.", cause)
    class InvalidRoomState(cause: Throwable? = null) :
        RoomError("The room is in an invalid state for this operation.", cause)
    class NetworkConnection(cause: Throwable? = null) :
        RoomError("Network connection problem. Please check your connection.", cause)
    class PermissionDenied(cause: Throwable? = null) :
        RoomError("You do not have permission to perform this action.", cause)
    class Unknown(cause: Throwable? = null) :
        RoomError("An unexpected error occurred with the user profile. Please try again.", cause)
}
