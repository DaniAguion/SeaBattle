package com.example.seabattle.data.firestore.errors

sealed class FirestoreError(message: String? = null, cause: Throwable? = null)
    : Exception(message, cause) {
        class NotFound(cause: Throwable? = null) :
            FirestoreError("Data could not be found.", cause)
        class NetworkConnection(cause: Throwable? = null) :
            FirestoreError("Network connection problem. Please check your connection.", cause)
        class PermissionDenied(cause: Throwable? = null) :
            FirestoreError("You do not have permission to perform this action.", cause)
        class Unknown(cause: Throwable? = null) :
            FirestoreError("An unexpected error occurred with the game. Please try again.", cause)
    }
