package com.example.seabattle.data.repository

import android.util.Patterns
import com.example.seabattle.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth

class AuthRepositoryImpl(private val auth: FirebaseAuth) : AuthRepository {

    override fun loginUser(email: String, password: String) {
        if (!checkFormats(email, password)) return
        auth.signInWithEmailAndPassword(email, password)
    }

    override fun checkAuthStatus() {
        val user = auth.currentUser
        if (user != null) {
            // Usuario autenticado
        } else {
            // Usuario no autenticado
        }
    }

    private fun checkFormats(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false
        }
        if (password.length < 6) {
            return false
        }
        return true
    }
}
