package com.example.seabattle.data.session

import android.util.Log
import com.example.seabattle.data.storage.SecurePrefsData
import com.example.seabattle.domain.auth.LoginMethod
import com.example.seabattle.domain.auth.repository.AuthRepository
import com.example.seabattle.domain.model.UserProfile

class SessionManager(
    private val securePrefs: SecurePrefsData,
    private val authRepository: AuthRepository
) {

    suspend fun registerUser(email: String, password: String) : Boolean {
        authRepository.registerUser(email, password)
        securePrefs.saveUserSession(userProfile = authRepository.getCurrentUser())
        return isLoggedIn()
    }

    suspend fun loginUser(loginMethod: LoginMethod) : Boolean{
        authRepository.loginUser(loginMethod)
        securePrefs.saveUserSession(userProfile = authRepository.getCurrentUser())
        return isLoggedIn()
    }


    fun logoutUser(){
        authRepository.logoutUser()
        securePrefs.clearSession()
    }


    fun isLoggedIn() : Boolean {
        return authRepository.isLoggedIn()
    }


    fun getUserProfile() : UserProfile? {
        if (authRepository.isLoggedIn()) {
            return UserProfile(
                uid = securePrefs.getUid(),
                displayName = securePrefs.getDisplayName(),
                email = securePrefs.getEmail(),
                photoUrl = securePrefs.getPhoto()
            )
        }
        return null
    }
}