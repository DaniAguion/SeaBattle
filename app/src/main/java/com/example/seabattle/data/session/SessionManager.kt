package com.example.seabattle.data.session

import com.example.seabattle.data.repository.AuthRepositoryImpl
import com.example.seabattle.data.storage.SecurePrefsData
import com.example.seabattle.domain.auth.LoginMethod
import com.example.seabattle.domain.model.UserProfile

class SessionManager(
    private val securePrefs: SecurePrefsData,
    private val authRepositoryImpl: AuthRepositoryImpl
) {

    fun syncSession() : Boolean {
        if (authRepositoryImpl.isLoggedIn()) {
            securePrefs.saveUserSession(userProfile = authRepositoryImpl.getCurrentUser())
            return true
        } else {
            securePrefs.clearSession()
            return false
        }
    }

    suspend fun loginUser(loginMethod: LoginMethod) : Boolean{
        authRepositoryImpl.loginUser(loginMethod)
        return syncSession()
    }

    suspend fun registerUser(email: String, password: String) : Boolean {
        authRepositoryImpl.registerUser(email, password)
        return syncSession()
    }

    fun logoutUser(){
        authRepositoryImpl.logoutUser()
        securePrefs.clearSession()
    }

    fun getUserProfile() : UserProfile? {
        if (authRepositoryImpl.isLoggedIn()) {
            return UserProfile(
                uid = securePrefs.getUid(),
                displayName = securePrefs.getDisplayName(),
                email = securePrefs.getEmail(),
                photoUrl = securePrefs.getPhoto()
            )
        } else {
            securePrefs.clearSession()
            return null
        }
    }

}