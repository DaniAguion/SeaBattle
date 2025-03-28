package com.example.seabattle.model

import com.example.seabattle.firebase.auth.AuthState

data class SesionState(
    val userAuthState: AuthState = AuthState.Unauthenticated,
)
