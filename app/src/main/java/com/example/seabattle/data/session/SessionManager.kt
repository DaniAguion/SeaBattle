package com.example.seabattle.data.session

import com.example.seabattle.data.repository.AuthRepositoryImpl
import com.example.seabattle.data.storage.SecurePrefsData
import com.example.seabattle.domain.auth.LoginMethod
import com.example.seabattle.domain.model.SessionState
import com.example.seabattle.domain.model.UserProfile

class SessionManager(
    private val securePrefs: SecurePrefsData,
    private val authRepositoryImpl: AuthRepositoryImpl
) {

    suspend fun loginUser(loginMethod: LoginMethod) : Boolean{
        authRepositoryImpl.loginUser(loginMethod)
        saveUserProfile()
        return authRepositoryImpl.isLoggedIn()
    }


    private fun saveUserProfile() {
        if (authRepositoryImpl.isLoggedIn()) {
            securePrefs.saveUserSession(userProfile = authRepositoryImpl.getCurrentUser())
        } else {
            securePrefs.clearSession()
        }
    }


    fun logoutUser(){
        authRepositoryImpl.logoutUser()
        securePrefs.clearSession()
    }


    suspend fun registerUser(email: String, password: String) : Boolean {
        authRepositoryImpl.registerUser(email, password)
        saveUserProfile()
        return authRepositoryImpl.isLoggedIn()
    }


    fun getUserProfile() : UserProfile? {
        if (authRepositoryImpl.isLoggedIn()) {
            return UserProfile(
                sessionState = SessionState.AUTH,
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