package com.example.seabattle.domain.repository

import com.example.seabattle.domain.entity.LoginMethod
import com.example.seabattle.domain.entity.User

interface AuthRepository {
    suspend fun registerUser(email: String, password: String) : Result<Boolean>
    suspend fun loginUser(method: LoginMethod) : Result<Boolean>
    suspend fun setUserName(userName: String) : Result<Unit>
    suspend fun deleteUser() : Result<Unit>
    fun logoutUser()
    fun isLoggedIn() : Boolean
    fun getAuthUserProfile(): Result<User>
}