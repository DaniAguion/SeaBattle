package com.example.seabattle.domain.auth.usecases

import com.example.seabattle.domain.auth.SessionManager

class CheckUserAuthUseCase (private val sessionManager: SessionManager ) {
    operator fun invoke(): Boolean {
        return sessionManager.isLoggedIn()
    }
}