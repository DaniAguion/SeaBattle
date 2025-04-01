package com.example.seabattle.domain.repository


interface AuthRepository {
    fun loginUser(email: String, password: String)
    fun checkAuthStatus()
}