package com.example.seabattle.domain.auth.usecase

import com.example.seabattle.data.session.SessionManager


class LogoutUserUseCase (private val sessionManager: SessionManager ) {
    operator fun invoke() {
        sessionManager.logoutUser()
    }
}