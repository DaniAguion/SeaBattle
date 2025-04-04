package com.example.seabattle.domain.auth.repository

import com.example.seabattle.domain.auth.LoginMethod
import com.example.seabattle.domain.model.User


interface AuthRepository {
    suspend fun loginUser(method: LoginMethod) : Boolean
    fun checkAuthStatus() : Boolean
}