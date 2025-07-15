package com.example.seabattle.domain.usecase.user

import com.example.seabattle.domain.SessionService
import kotlinx.coroutines.flow.Flow


class ListenUserUseCase (
    private val sessionService: SessionService,
) {
    operator fun invoke(): Flow<String?> {
        return sessionService.currentUserId
    }
}