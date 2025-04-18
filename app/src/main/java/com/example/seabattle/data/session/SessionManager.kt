package com.example.seabattle.data.session

import android.util.Log
import com.example.seabattle.data.storage.SecurePrefsData
import com.example.seabattle.domain.auth.LoginMethod
import com.example.seabattle.domain.auth.repository.AuthRepository
import com.example.seabattle.domain.firestore.repository.FirestoreRepository
import com.example.seabattle.domain.model.UserProfile


class SessionManager(
    private val securePrefs: SecurePrefsData,
    private val authRepository: AuthRepository,
    private val fireStoreRepository: FirestoreRepository
) {

    suspend fun registerUser(username: String, email: String, password: String) : Boolean {
        if (authRepository.registerUser(email, password)) {
            authRepository.setUserName(username)

            val userProfile = authRepository.getCurrentUser()
            uploadUserProfile(userProfile = userProfile)
            securePrefs.saveUserSession(userProfile = userProfile)

            return isLoggedIn()
        } else {
            Log.d("SessionManager", "User registration failed")
            return false
        }
    }

    private suspend fun uploadUserProfile(userProfile: UserProfile?) {
        // Check if userProfile is null or already exists in Firestore
        if (userProfile == null || fireStoreRepository.getUserProfile(userId = userProfile.uid) != null) return
        fireStoreRepository.createUserProfile(
            userId = userProfile.uid,
            userProfile = userProfile
        )
    }

    suspend fun loginUser(loginMethod: LoginMethod) : Boolean{
        authRepository.loginUser(loginMethod)
        val userProfile = authRepository.getCurrentUser()
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