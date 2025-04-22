package com.example.seabattle.domain.auth.usecases

import com.example.seabattle.data.session.SessionManager


class LogoutUserUseCase (private val sessionManager: SessionManager ) {
    operator fun invoke() {
        sessionManager.logoutUser()
    }
}