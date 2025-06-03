package com.example.seabattle.data.firestore.errors

import com.example.seabattle.domain.errors.GameError
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreException.Code


fun Throwable.toGameError(): GameError {
    if (this is GameError) {
        return this
    }

    return when (this) {
        is FirebaseFirestoreException -> {
            when (this.code) {
                Code.NOT_FOUND -> GameError.GameNotFound(this)
                Code.PERMISSION_DENIED -> GameError.PermissionDenied(this)
                Code.UNAUTHENTICATED -> GameError.PermissionDenied(this)
                Code.UNAVAILABLE -> GameError.NetworkConnection(this)
                else -> {
                    GameError.Unknown(this)
                }
            }
        }
        else -> {
            GameError.Unknown(this)
        }
    }
}

