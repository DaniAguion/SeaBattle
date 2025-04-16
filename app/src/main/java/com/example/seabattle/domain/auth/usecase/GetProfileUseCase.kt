package com.example.seabattle.domain.auth.usecase

import com.example.seabattle.data.session.SessionManager
import com.example.seabattle.domain.model.UserProfile

class GetProfileUseCase (private val sessionManager: SessionManager ) {
    operator fun invoke(): UserProfile? {
        return sessionManager.getUserProfile()
    }
}