package com.example.seabattle.data.firestore.dto

data class UserDto(
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val status: String = "offline",
    val score: Int = 1000 // Default score value
)
