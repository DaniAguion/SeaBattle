package com.example.seabattle.domain.auth.usecase

import com.example.seabattle.data.session.SessionManager

class RegisterUserUseCase (private val sessionManager: SessionManager) {
    suspend operator fun invoke(email: String, password: String): Boolean {
        return sessionManager.registerUser(email, password)
    }
}