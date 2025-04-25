package com.example.seabattle.domain.model

data class User(
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val score: Int = 0,
    val online: Boolean = false,
    val lookingForGame: Boolean = false,
    val inGame: Boolean = false,
)

// Lightweight version of User for local storage
data class LocalUser(
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String = "",
)

