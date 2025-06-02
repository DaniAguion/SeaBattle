package com.example.seabattle.data.firestore.errors

import com.example.seabattle.domain.errors.UserError
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreException.Code


// Extension function to map FirebaseAuthException to AuthError
fun Throwable.toUserError(): UserError {
    if (this is UserError) {
        return this
    }

    return when (this) {
        is FirebaseFirestoreException -> {
            when (this.code) {
                Code.NOT_FOUND -> UserError.UserProfileNotFound(this)
                Code.PERMISSION_DENIED -> UserError.PermissionDenied(this)
                Code.UNAUTHENTICATED -> UserError.PermissionDenied(this)
                Code.UNAVAILABLE -> UserError.NetworkConnection(this)
                else -> {
                    UserError.Unknown(this)
                }
            }
        }
        else -> {
            UserError.Unknown(this)
        }
    }
}

