package com.example.seabattle.domain.repository


interface AuthRepository {
    suspend fun loginUser(email: String, password: String) : Boolean
    fun checkAuthStatus() : Boolean
}