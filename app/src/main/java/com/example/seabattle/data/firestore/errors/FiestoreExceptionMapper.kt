package com.example.seabattle.data.firestore.errors

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreException.Code


// Extension function to map FirebaseAuthException to class FirestoreError
fun Throwable.toFirestoreError(): FirestoreError {
    if (this is FirestoreError) {
        return this
    }

    return when (this) {
        is FirebaseFirestoreException -> {
            when (this.code) {
                Code.NOT_FOUND -> FirestoreError.NotFound(this)
                Code.PERMISSION_DENIED -> FirestoreError.PermissionDenied(this)
                Code.UNAUTHENTICATED -> FirestoreError.PermissionDenied(this)
                Code.UNAVAILABLE -> FirestoreError.NetworkConnection(this)
                else -> {
                    FirestoreError.Unknown(this)
                }
            }
        }
        else -> {
            FirestoreError.Unknown(this)
        }
    }
}

