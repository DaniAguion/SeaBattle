package com.example.seabattle.domain.usecase.auth

import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.entity.User
import kotlinx.coroutines.flow.Flow


class ListenUserUseCase (
    private val sessionService: SessionService,
) {
    operator fun invoke(): Flow<User?> {
        return sessionService.currentUser
    }
}