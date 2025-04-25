package com.example.seabattle.domain.auth

import com.example.seabattle.domain.model.User

interface AuthRepository {
    suspend fun registerUser(email: String, password: String) : Boolean
    suspend fun loginUser(method: LoginMethod) : Boolean
    fun logoutUser()
    fun isLoggedIn() : Boolean
    fun getAuthUserProfile() : User?
    suspend fun setUserName(userName: String)
}