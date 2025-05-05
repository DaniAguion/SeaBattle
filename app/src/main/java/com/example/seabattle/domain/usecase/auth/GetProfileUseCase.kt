package com.example.seabattle.domain.usecase.auth

import com.example.seabattle.domain.services.SessionManager
import com.example.seabattle.domain.entity.UserLocal

class GetProfileUseCase (private val sessionManager: SessionManager ) {
    operator fun invoke(): UserLocal {
        return sessionManager.getLocalUserProfile()
    }
}