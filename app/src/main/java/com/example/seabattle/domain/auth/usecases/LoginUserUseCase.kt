package com.example.seabattle.domain.auth.usecases

import com.example.seabattle.domain.auth.SessionManager
import com.example.seabattle.domain.auth.LoginMethod


class LoginUserUseCase ( private val sessionManager: SessionManager ) {
    suspend operator fun invoke(method: LoginMethod): Boolean {
        return sessionManager.loginUser(method)
    }
}