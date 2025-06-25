package com.example.seabattle.domain.usecase.user

import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.repository.AuthRepository

class LogoutUserUseCase (
    private val authRepository: AuthRepository,
    private val sessionService: SessionService,
) {
    operator fun invoke() {
        authRepository.logoutUser()
        sessionService.clearCurrentUser()
    }
}