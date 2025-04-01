package com.example.seabattle.data.repository

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.example.seabattle.domain.repository.AuthRepository
import com.example.seabattle.firebase.auth.AuthState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.channels.awaitClose


class AuthRepositoryImpl(private val auth: FirebaseAuth) : AuthRepository {
    private val _uiState = MutableLiveData<AuthState>()

    override fun loginUser(email: String, password: String): Flow<AuthState> = callbackFlow {
        if (!checkFormats(email, password)) {
            trySend(AuthState.Error("Invalid email or password format"))
            close()
            return@callbackFlow
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(AuthState.Authenticated)
                } else {
                    trySend(AuthState.Error(task.exception?.message ?: "Login failed"))
                }
                close()
            }
        awaitClose { }
    }

    override fun registerUser(email: String, password: String): Flow<AuthState> = callbackFlow {
        if (!checkFormats(email, password)) {
            trySend(AuthState.Error("Invalid email or password format"))
            close()
            return@callbackFlow
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(AuthState.Authenticated)
                } else {
                    trySend(AuthState.Error(task.exception?.message ?: "Registration failed"))
                }
                close()
            }
        awaitClose { }
    }

    override fun signOut(): Flow<AuthState> = flow {
        auth.signOut()
        emit(AuthState.Unauthenticated)
    }

    override fun checkAuthStatus(): Flow<AuthState> = flow {
        val user = auth.currentUser
        if (user != null) {
            emit(AuthState.Authenticated)
        } else {
            emit(AuthState.Unauthenticated)
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

