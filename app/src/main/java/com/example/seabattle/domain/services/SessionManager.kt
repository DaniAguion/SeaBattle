package com.example.seabattle.domain.services

import android.util.Log
import com.example.seabattle.data.local.SecurePrefsData
import com.example.seabattle.domain.entity.LoginMethod
import com.example.seabattle.domain.repository.UserRepository
import com.example.seabattle.domain.entity.UserLocal
import com.example.seabattle.domain.entity.User
import com.example.seabattle.domain.repository.AuthRepository

class SessionManager(
    private val securePrefs: SecurePrefsData,
    private val authRepository: AuthRepository,
    private val fireStoreRepository: UserRepository
) {
    private suspend fun uploadUserProfile(user: User) {
        // Check if userProfile is null or already exists in Firestore
    }

    fun logoutUser(){
        authRepository.logoutUser()
        securePrefs.clearSession()
    }

    fun isLoggedIn() : Boolean {
        return authRepository.isLoggedIn()
    }

    // This function is used to get the user profile from Firestore
    suspend fun getFireStoreUserProfile() : User? {
        val userId = securePrefs.getUid()
        //return fireStoreRepository.getUser(userId)
        return User()
    }

    // This function is used to get the user profile for UI display in ProfileScreen
    fun getLocalUserProfile() : UserLocal {
        return UserLocal(
            userId = securePrefs.getUid(),
            displayName = securePrefs.getDisplayName(),
            email = securePrefs.getEmail(),
            photoUrl = securePrefs.getPhoto()
        )
    }
}