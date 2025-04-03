package com.example.seabattle.data.repository

import android.util.Log
import com.example.seabattle.data.storage.SecurePrefsData
import com.example.seabattle.domain.model.SessionState
import com.example.seabattle.domain.model.UserProfile
import com.example.seabattle.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val auth: FirebaseAuth
) : AuthRepository {

    override suspend fun loginUser(email: String, password: String) : Boolean {
        try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            return (authResult.user != null)
        } catch (e: Exception) {
            return false
        }
    }


    override fun logoutUser() : Unit {
        auth.signOut()
    }


    override fun isLoggedIn() : Boolean {
        return (auth.currentUser != null)
    }


    override fun getCurrentUser(): UserProfile {
        val user = auth.currentUser
        return user?.let {
            UserProfile(
                sessionState = SessionState.AUTH,
                uid = it.uid,
                displayName = it.displayName ?: "",
                email = it.email ?: "",
                photoUrl = it.photoUrl?.toString() ?: ""
            )
        } ?: UserProfile(SessionState.NO_AUTH, "-1", "", "", "")
    }


    override suspend fun registerUser(email: String, password: String) : Boolean {
        try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            return (authResult.user != null)
        } catch (e: Exception) {
            return false
        }
    }
}
