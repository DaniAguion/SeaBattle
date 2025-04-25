package com.example.seabattle.data.firestore.entities

data class UserEntity(
    val userId: String,
    val displayName: String,
    val email: String,
    val photoUrl: String,
    val score: Int,
    val online: Boolean,
    val lookingForGame: Boolean,
    val inGame: Boolean,
)
