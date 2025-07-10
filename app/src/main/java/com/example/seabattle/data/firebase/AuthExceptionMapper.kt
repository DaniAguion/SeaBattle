package com.example.seabattle.data.firebase

import com.example.seabattle.domain.errors.AuthError
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException


// Extension function to map FirebaseAuthException to AuthError
fun Throwable.toAuthError(): AuthError {
    if (this is AuthError) {
        return this
    }

    return when (this) {
        is FirebaseAuthInvalidCredentialsException -> AuthError.InvalidCredentials(this)
        is FirebaseAuthInvalidUserException -> AuthError.InvalidUser(this)
        is FirebaseAuthUserCollisionException -> AuthError.UserCollision(this)
        is FirebaseNetworkException -> AuthError.NetworkConnection(this)
        is FirebaseAuthRecentLoginRequiredException -> AuthError.RecentLoginRequired(this)
        is FirebaseException -> AuthError.Unknown(this)
        else -> AuthError.Unknown(this)
    }
}
