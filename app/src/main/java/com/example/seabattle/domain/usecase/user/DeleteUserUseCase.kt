package com.example.seabattle.domain.usecase.user

import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.repository.AuthRepository
import com.example.seabattle.domain.repository.PresenceRepository
import com.example.seabattle.domain.repository.UserRepository

class DeleteUserUseCase (
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val presenceRepo: PresenceRepository,
    private val sessionService: SessionService,
) {
    suspend operator fun invoke() {
        val userId = sessionService.getCurrentUserId()

        // Delete user from all repositories
        presenceRepo.deleteUserPresence(userId = userId).getOrThrow()
        userRepository.deleteUser(userId = userId).getOrThrow()
        authRepository.deleteUser().getOrThrow()
        sessionService.clearCurrentUser()
    }
}