package com.example.seabattle.domain.repository

import com.example.seabattle.domain.model.SessionState
import com.example.seabattle.domain.model.UserProfile


interface AuthRepository {
    suspend fun loginUser(email: String, password: String) : Boolean
    fun logoutUser() : Unit
    fun isLoggedIn() : Boolean
    fun getCurrentUser() : UserProfile
    suspend fun registerUser(email: String, password: String) : Boolean
}