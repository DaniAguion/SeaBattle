package com.example.seabattle.domain.auth.usecase

import com.example.seabattle.data.session.SessionManager

class CheckUserAuthUseCase (private val sessionManager: SessionManager ) {
    operator fun invoke(): Boolean {
        return sessionManager.isLoggedIn()
    }
}