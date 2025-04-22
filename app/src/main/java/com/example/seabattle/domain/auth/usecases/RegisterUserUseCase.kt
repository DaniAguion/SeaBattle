package com.example.seabattle.domain.auth.usecases

import com.example.seabattle.data.SessionManager

class RegisterUserUseCase (private val sessionManager: SessionManager) {
    suspend operator fun invoke(username: String, email: String, password: String): Boolean {
        return sessionManager.registerUser(username, email, password)
    }
}