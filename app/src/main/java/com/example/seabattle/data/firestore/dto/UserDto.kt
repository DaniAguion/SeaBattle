package com.example.seabattle.data.firestore.dto

data class UserDto(
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val score: Int = 0
)
