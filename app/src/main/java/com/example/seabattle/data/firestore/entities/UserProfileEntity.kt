package com.example.seabattle.data.firestore.entities

data class UserProfileEntity(
    val userId: String,
    val displayName: String,
    val email: String,
    val photoUrl: String,
    val score: Int,
    val isOnline: Boolean,
    val lookingForGame: Boolean,
    val inGame: Boolean,
)
