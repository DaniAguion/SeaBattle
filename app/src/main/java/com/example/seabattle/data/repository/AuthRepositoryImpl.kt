package com.example.seabattle.data.repository

import android.util.Log
import android.util.Patterns
import com.example.seabattle.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(private val auth: FirebaseAuth) : AuthRepository {

    override suspend fun loginUser(email: String, password: String) : Boolean {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val loginResult = authResult.user != null
            Log.d("AuthRepositoryImpl", "Logged In $loginResult")
            loginResult
        } catch (e: Exception) {
            false
        }
    }

    override fun checkAuthStatus() {
        val user = auth.currentUser
        if (user != null) {
            // Usuario autenticado
        } else {
            // Usuario no autenticado
        }
    }
}
