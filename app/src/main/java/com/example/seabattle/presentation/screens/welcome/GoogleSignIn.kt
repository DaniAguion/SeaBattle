package com.example.seabattle.presentation.screens.welcome

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.example.seabattle.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import timber.log.Timber

class GoogleSignIn() {
    private lateinit var credentialManager: CredentialManager

    suspend fun signIn(context: Context) : String? {
        try {
            initializeCredentialManager(context)
            val result = buildCredentialRequest(context)
            return handleSignIn(result)
        } catch (e: Exception) {
            Timber.e("Failed to get credential: ${e.localizedMessage}")
        }
        return null
    }

    private fun initializeCredentialManager(context: Context) {
        this.credentialManager = CredentialManager.Companion.create(context)
    }

    suspend private fun buildCredentialRequest(context: Context): GetCredentialResponse {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(ContextCompat.getString(context, R.string.default_web_client_id))
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return credentialManager.getCredential(context, request)
    }

    private fun handleSignIn(result: GetCredentialResponse) : String? {
        val credential = result.credential

        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.Companion.createFrom(credential.data)
            Timber.d("Google ID Token ${googleIdTokenCredential.idToken}")
            return googleIdTokenCredential.idToken
        } else {
            Timber.e("Error. Credential is not of type Google ID!")
        }
        return null
    }
}