package com.example.seabattle.domain.usecase.user

import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.repository.AuthRepository
import com.example.seabattle.domain.repository.PresenceRepository

class LogoutUserUseCase (
    private val authRepository: AuthRepository,
    private val presenceRepo: PresenceRepository,
    private val sessionService: SessionService,
) {
    suspend operator fun invoke() {
        val userId = sessionService.getCurrentUserId()

        // Set user presence status to offline before logging out
        presenceRepo.setUserOffline(userId = userId).getOrThrow()

        authRepository.logoutUser()
        sessionService.clearCurrentUser()
    }
}