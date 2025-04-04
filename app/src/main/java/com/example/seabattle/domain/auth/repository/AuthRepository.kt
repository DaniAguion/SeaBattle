package com.example.seabattle.domain.auth.repository

import com.example.seabattle.domain.auth.LoginMethod
import com.example.seabattle.domain.model.UserProfile


interface AuthRepository {
    suspend fun loginUser(method: LoginMethod) : Boolean
    fun logoutUser() : Unit
    fun isLoggedIn() : Boolean
    fun getCurrentUser() : UserProfile
    suspend fun registerUser(email: String, password: String) : Boolean
}