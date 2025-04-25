package com.example.seabattle.domain.auth.usecases

import com.example.seabattle.domain.auth.SessionManager
import com.example.seabattle.domain.model.LocalUser
import com.example.seabattle.domain.model.User

class GetProfileUseCase (private val sessionManager: SessionManager ) {
    operator fun invoke(): LocalUser {
        return sessionManager.getLocalUserProfile()
    }
}