package com.example.seabattle.data.repository

import android.util.Log
import com.example.seabattle.domain.auth.LoginMethod
import com.example.seabattle.domain.auth.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val auth: FirebaseAuth
) : AuthRepository {

    override suspend fun loginUser(method: LoginMethod) : Boolean {
        when (method) {
            is LoginMethod.EmailPassword -> {
                try {
                    val authResult = auth.signInWithEmailAndPassword(method.email, method.password).await()
                    val loginResult = authResult.user != null
                    Log.d("AuthRepositoryImpl", "Logged In $loginResult")
                    return loginResult
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

    override fun checkAuthStatus() : Boolean {
        val user = auth.currentUser
        return (user != null)
    }
}
