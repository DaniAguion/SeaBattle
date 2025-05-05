package com.example.seabattle.domain.usecase.auth

import com.example.seabattle.domain.services.SessionManager
import com.example.seabattle.domain.entity.LoginMethod


class LoginUserUseCase ( private val sessionManager: SessionManager ) {
    suspend operator fun invoke(method: LoginMethod): Boolean {
        return sessionManager.loginUser(method)
    }
}