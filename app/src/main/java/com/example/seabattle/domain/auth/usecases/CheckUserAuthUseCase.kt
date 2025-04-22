package com.example.seabattle.domain.auth.usecases

import com.example.seabattle.data.SessionManager

class CheckUserAuthUseCase (private val sessionManager: SessionManager ) {
    operator fun invoke(): Boolean {
        return sessionManager.isLoggedIn()
    }
}