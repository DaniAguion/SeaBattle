package com.example.seabattle.domain.entity

data class User(
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val status: String = "offline",
    val score: Int = 1000
)



