package com.example.seabattle.domain.usecase.auth

import com.example.seabattle.domain.services.SessionManager


class LogoutUserUseCase (private val sessionManager: SessionManager ) {
    operator fun invoke() {
        sessionManager.logoutUser()
    }
}