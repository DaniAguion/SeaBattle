package com.example.seabattle.domain.usecase.auth

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.repository.AuthRepository

class LogoutUserUseCase (
    private val authRepository: AuthRepository,
    private val session: Session,
) {
    operator fun invoke() {
        authRepository.logoutUser()
        session.clearCurrentUser()
    }
}