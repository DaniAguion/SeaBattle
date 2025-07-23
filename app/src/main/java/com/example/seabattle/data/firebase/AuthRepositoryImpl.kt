package com.example.seabattle.data.firebase


import com.example.seabattle.domain.entity.LoginMethod
import com.example.seabattle.domain.repository.AuthRepository
import com.example.seabattle.domain.entity.User
import com.example.seabattle.domain.errors.AuthError
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


    // Method to register a new user with email and password
    override suspend fun registerUser(email: String, password: String) : Result<Boolean>
    = withContext(ioDispatcher) {
        runCatching {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            return@runCatching authResult.user != null
        }
        .recoverCatching { throwable ->
            throw throwable.toAuthError()
        }
    }


    // Method to login a user using either email/password or Google sign-in
    override suspend fun loginUser(method: LoginMethod) : Result<Boolean>
    = withContext(ioDispatcher) {
        runCatching {
            val authResult = when (method) {
                is LoginMethod.EmailPassword -> {
                    auth.signInWithEmailAndPassword(method.email, method.password).await()
                }

                is LoginMethod.Google -> {
                    val credential = GoogleAuthProvider.getCredential(method.googleIdToken, null)
                    auth.signInWithCredential(credential).await()
                }
            }
            return@runCatching authResult.user != null
        }
        .recoverCatching { throwable ->
            throw throwable.toAuthError()
        }
    }


    // Method to update the user's display name
    override suspend fun setUserName(userName: String) : Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val user = auth.currentUser
            if (user == null) {
                throw AuthError.InvalidUser()
            }
            val profileUpdates = userProfileChangeRequest { displayName = userName }
            user.updateProfile(profileUpdates).await()
            return@runCatching
        }
        .recoverCatching { throwable ->
            throw throwable.toAuthError()
        }
    }


    // Method to delete the current user
    override suspend fun deleteUser() : Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val user = auth.currentUser ?: throw AuthError.InvalidUser()
            user.delete().await()
            return@runCatching
        }
        .recoverCatching { throwable ->
            throw throwable.toAuthError()
        }
    }


    // Method to logout the current user
    override fun logoutUser() {
        auth.signOut()
    }


    // Method to check if a user is currently logged in
    override fun isLoggedIn() : Boolean {
        return (auth.currentUser != null)
    }


    // Method to get the current user's profile
    override fun getAuthUserProfile(): Result<User> {
        return runCatching {
            val user = auth.currentUser ?: throw AuthError.InvalidUser()
            User(
                userId = user.uid,
                displayName = user.displayName ?: user.providerData[0]?.displayName ?: "",
                email =  user.email ?: user.providerData[0]?.email ?: "",
                photoUrl = (user.photoUrl ?: user.providerData[0]?.photoUrl ?: "").toString()
            )
        }
        .recoverCatching { throwable ->
            throw throwable.toAuthError()
        }
    }


    override suspend fun askForPasswordReset(email: String): Result<Unit>  = withContext(ioDispatcher) {
        runCatching {
            auth.sendPasswordResetEmail(email).await()
            return@runCatching
        }
        .recoverCatching { throwable ->
            throw throwable.toAuthError()
        }
    }
}
