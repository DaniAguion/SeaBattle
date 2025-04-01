package com.example.seabattle.model

import com.example.seabattle.domain.model.User

data class SesionState(
    val user: User,
    val authState : Boolean
)
