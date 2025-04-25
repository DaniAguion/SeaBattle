package com.example.seabattle.domain.auth.usecases

import com.example.seabattle.domain.auth.SessionManager
import com.example.seabattle.domain.model.UserLocal

class GetProfileUseCase (private val sessionManager: SessionManager ) {
    operator fun invoke(): UserLocal {
        return sessionManager.getLocalUserProfile()
    }
}