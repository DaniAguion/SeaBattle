package com.example.seabattle.data.firebase


import android.util.Log
import com.example.seabattle.domain.entity.LoginMethod
import com.example.seabattle.domain.repository.AuthRepository
import com.example.seabattle.domain.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val ioDispatcher: CoroutineDispatcher
) : AuthRepository {


    override suspend fun registerUser(email: String, password: String) : Result<Boolean>
    = withContext(ioDispatcher) {
        runCatching {
            auth.createUserWithEmailAndPassword(email, password).await()
        }
        .map { authResult ->
            authResult.user != null
        }
        .onFailure { e ->
            Log.e("AuthRepository", "Register with email and password failed", e)
        }
    }



    override suspend fun loginUser(method: LoginMethod) : Result<Boolean>
    = withContext(ioDispatcher) {
        runCatching {
            when (method) {
                is LoginMethod.EmailPassword -> {
                    auth.signInWithEmailAndPassword(method.email, method.password).await()
                }

                is LoginMethod.Google -> {
                    val credential = GoogleAuthProvider.getCredential(method.googleIdToken, null)
                    auth.signInWithCredential(credential).await()
                }
            }
        }
        .map { authResult ->
            authResult.user != null
        }
        .onFailure { e ->
            Log.e("AuthRepository", "Login failed", e)
        }
    }


    override fun logoutUser() {
        auth.signOut()
    }


    override fun isLoggedIn() : Boolean {
        return (auth.currentUser != null)
    }


    override fun getAuthUserProfile(): User? {
        val user = auth.currentUser
        return user?.let {
            User(
                userId = it.uid,
                displayName = it.displayName ?: it.providerData[0]?.displayName ?: "",
                email =  it.email ?: it.providerData[0]?.email ?: "",
                photoUrl = (it.photoUrl ?: it.providerData[0]?.photoUrl ?: "").toString()
            )
        }
        return null
    }


    override suspend fun setUserName(userName: String) : Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val user = auth.currentUser
            if (user == null) {
                throw IllegalStateException("User is null")
            }
            val profileUpdates = userProfileChangeRequest {
                displayName = userName
            }
             user.updateProfile(profileUpdates).await()
        }.map{ _ -> Unit}
    }
}
