package com.example.seabattle.domain.model

data class UserProfile(
    val sessionState: SessionState,
    val uid: String,
    val displayName: String,
    val email: String,
    val photoUrl: String,
)

enum class SessionState {
    ANON,
    NO_AUTH,
    AUTH,
}