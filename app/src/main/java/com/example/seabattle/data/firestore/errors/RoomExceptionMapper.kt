package com.example.seabattle.data.firestore.errors

import com.example.seabattle.domain.errors.RoomError
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreException.Code


fun Throwable.toRoomError(): RoomError {
    if (this is RoomError) {
        return this
    }

    return when (this) {
        is FirebaseFirestoreException -> {
            when (this.code) {
                Code.NOT_FOUND -> RoomError.RoomNotFound(this)
                Code.PERMISSION_DENIED -> RoomError.PermissionDenied(this)
                Code.UNAUTHENTICATED -> RoomError.PermissionDenied(this)
                Code.UNAVAILABLE -> RoomError.NetworkConnection(this)
                else -> {
                    RoomError.Unknown(this)
                }
            }
        }
        else -> {
            RoomError.Unknown(this)
        }
    }
}

