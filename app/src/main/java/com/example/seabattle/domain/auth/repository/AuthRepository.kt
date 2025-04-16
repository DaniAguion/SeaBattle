package com.example.seabattle.domain.auth.repository

import com.example.seabattle.domain.auth.LoginMethod
import com.example.seabattle.domain.model.UserProfile


interface AuthRepository {
    suspend fun registerUser(email: String, password: String) : Boolean
    suspend fun loginUser(method: LoginMethod) : Boolean
    fun logoutUser()
    fun isLoggedIn() : Boolean
    fun getCurrentUser() : UserProfile?
    suspend fun setUserName(userName: String)
}