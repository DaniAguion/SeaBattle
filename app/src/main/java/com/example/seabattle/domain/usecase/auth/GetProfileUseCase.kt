package com.example.seabattle.domain.usecase.auth

import com.example.seabattle.data.local.SecurePrefsData
import com.example.seabattle.domain.entity.UserLocal

class GetProfileUseCase (private val securePrefs: SecurePrefsData ) {
    operator fun invoke(): UserLocal {
        return UserLocal(
            userId = securePrefs.getUid(),
            displayName = securePrefs.getDisplayName(),
            email = securePrefs.getEmail(),
            photoUrl = securePrefs.getPhoto()
        )
    }
}