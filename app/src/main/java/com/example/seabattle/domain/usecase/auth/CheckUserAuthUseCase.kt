package com.example.seabattle.domain.usecase.auth

import com.example.seabattle.domain.services.SessionManager

class CheckUserAuthUseCase (private val sessionManager: SessionManager ) {
    operator fun invoke(): Boolean {
        return sessionManager.isLoggedIn()
    }
}