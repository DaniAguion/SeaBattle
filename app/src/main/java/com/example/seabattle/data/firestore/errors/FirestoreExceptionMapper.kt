package com.example.seabattle.data.firestore.errors

import com.example.seabattle.domain.errors.DataError
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreException.Code


// Extension function to map FirebaseAuthException to class FirestoreError
fun Throwable.toDataError(): DataError {
    if (this is DataError) {
        return this
    }

    return when (this) {
        is FirebaseFirestoreException -> {
            when (this.code) {
                Code.NOT_FOUND -> DataError.NotFound(this)
                Code.PERMISSION_DENIED -> DataError.PermissionDenied(this)
                Code.UNAUTHENTICATED -> DataError.PermissionDenied(this)
                Code.UNAVAILABLE -> DataError.NetworkConnection(this)
                else -> {
                    DataError.Unknown(this)
                }
            }
        }
        else -> {
            DataError.Unknown(this)
        }
    }
}

