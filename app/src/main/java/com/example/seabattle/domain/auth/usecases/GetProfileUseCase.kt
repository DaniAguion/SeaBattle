package com.example.seabattle.domain.auth.usecases

import com.example.seabattle.domain.auth.SessionManager
import com.example.seabattle.domain.model.UserProfile

class GetProfileUseCase (private val sessionManager: SessionManager ) {
    operator fun invoke(): UserProfile {
        return sessionManager.getLocalUserProfile()
    }
}