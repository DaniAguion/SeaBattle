package com.example.seabattle.data.repository

import com.example.seabattle.domain.auth.LoginMethod
import com.example.seabattle.domain.auth.repository.AuthRepository
import com.example.seabattle.domain.model.SessionState
import com.example.seabattle.domain.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(private val auth: FirebaseAuth) : AuthRepository {

    override suspend fun loginUser(method: LoginMethod) : Boolean {
        when (method) {
            is LoginMethod.EmailPassword -> {
                try {
                    val authResult = auth.signInWithEmailAndPassword(method.email, method.password).await()
                    return (authResult.user != null)
                } catch (e: Exception) {
                    return false
                }
            }
            is LoginMethod.Google -> {
                // Handle Google login
                return false
            }
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
