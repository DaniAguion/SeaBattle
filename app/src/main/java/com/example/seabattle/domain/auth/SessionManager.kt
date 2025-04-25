package com.example.seabattle.domain.auth

import android.util.Log
import com.example.seabattle.data.storage.SecurePrefsData
import com.example.seabattle.domain.firestore.FirestoreRepository
import com.example.seabattle.domain.model.UserProfile

class SessionManager(
    private val securePrefs: SecurePrefsData,
    private val authRepository: AuthRepository,
    private val fireStoreRepository: FirestoreRepository
) {

    suspend fun registerUser(username: String, email: String, password: String) : Boolean {
        if (authRepository.registerUser(email, password)) {
            authRepository.setUserName(username)

            val userProfile = authRepository.getAuthUserProfile()
            if (userProfile == null) {
                Log.e("SessionManager", "User profile is null after registration")
                return false
            }
            uploadUserProfile(userProfile = userProfile)
            securePrefs.saveUserSession(userProfile = userProfile)

            return isLoggedIn()
        } else {
            Log.e("SessionManager", "User registration failed")
            return false
        }
    }

    private suspend fun uploadUserProfile(userProfile: UserProfile) {
        // Check if userProfile is null or already exists in Firestore
        if (fireStoreRepository.getUserProfile(userId = userProfile.userId) != null) return

        fireStoreRepository.createUserProfile(userProfile = userProfile)
    }

    suspend fun loginUser(loginMethod: LoginMethod) : Boolean{
        authRepository.loginUser(loginMethod)
        val userProfile = authRepository.getAuthUserProfile()
        if (userProfile == null) {
            Log.e("SessionManager", "User profile is null after login")
            return false
        }
        if (loginMethod is LoginMethod.Google) { uploadUserProfile(userProfile) }
        securePrefs.saveUserSession(userProfile)
        return isLoggedIn()
    }


    fun logoutUser(){
        authRepository.logoutUser()
        securePrefs.clearSession()
    }


    fun isLoggedIn() : Boolean {
        return authRepository.isLoggedIn()
    }

    // This function is used to get the user profile from Firestore
    suspend fun getFireStoreUserProfile(userId: String) : UserProfile? {
        return fireStoreRepository.getUserProfile(userId)
    }

    // This function is used to get the user profile for UI display in ProfileScreen
    fun getLocalUserProfile() : UserProfile {
        return UserProfile(
            userId = securePrefs.getUid(),
            displayName = securePrefs.getDisplayName(),
            email = securePrefs.getEmail(),
            photoUrl = securePrefs.getPhoto()
        )
    }
}