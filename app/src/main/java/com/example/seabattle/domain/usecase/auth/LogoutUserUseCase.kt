package com.example.seabattle.domain.usecase.auth

import com.example.seabattle.data.local.SecurePrefsData
import com.example.seabattle.domain.repository.AuthRepository

class LogoutUserUseCase (
    private val authRepository: AuthRepository,
    private val securePrefs: SecurePrefsData,
) {
    operator fun invoke() {
        authRepository.logoutUser()
        securePrefs.clearSession()
    }
}