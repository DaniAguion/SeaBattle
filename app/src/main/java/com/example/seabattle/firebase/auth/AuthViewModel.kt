package com.example.seabattle.firebase.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import android.util.Patterns

class AuthViewModel(private val auth: FirebaseAuth) : ViewModel() {
    private val _uiState = MutableLiveData<AuthState>()

    init{
        checkAuthStatus()
    }

    fun checkAuthStatus() : AuthState {
        val user = auth.currentUser
        if (user != null) {
            _uiState.value = AuthState.Authenticated
        } else {
            _uiState.value = AuthState.Unauthenticated
        }
        return _uiState.value ?: AuthState.Unauthenticated
    }

    fun signIn(email: String, password: String) {
        if (!checkFormats(email, password)) return

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.value = AuthState.Authenticated
                } else {
                    _uiState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }


    fun signUp(email: String, password: String) {
        if (!checkFormats(email, password)) return

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.value = AuthState.Authenticated
                } else {
                    _uiState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    private fun checkFormats(email: String, password: String) : Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            _uiState.value = AuthState.Error("Empty fields")
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = AuthState.Error("Invalid email format")
            return false
        }
        if (password.length < 6) {
            _uiState.value = AuthState.Error("Invalid password length")
            return false
        }
        return true
    }

    fun signOut() {
        auth.signOut()
        _uiState.value = AuthState.Unauthenticated
    }
}

sealed class AuthState{
    object Authenticated : AuthState() {
        private val user = FirebaseAuth.getInstance().currentUser
        val userName = user?.displayName
        val photo = user?.photoUrl
        val uid = user?.uid
    }
    object Unauthenticated : AuthState()
    data class Error(val message: String) : AuthState()
}

