package com.example.seabattle.domain.entity

data class User(
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val status: String = "offline",
    val score: Int = 0
)

// Basic version of User to attach to Game class
data class UserBasic(
    val userId: String = "",
    val displayName: String = "",
    val photoUrl: String = "",
    val status: String = ""
)

fun User.toBasic(): UserBasic {
    return UserBasic(
        userId = userId,
        displayName = displayName,
        photoUrl = photoUrl,
        status = status
    )
}


