package com.example.seabattle.ui.welcome

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.example.seabattle.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class GoogleSignIn() {
    private lateinit var context : Context
    private lateinit var credentialManager: CredentialManager

    suspend fun signIn(context: Context) : String? {
        try {
            initializeCredentialManager(context)
            val result = buildCredentialRequest()
            return handleSignIn(result)
        } catch (e: Exception) {
            Log.e("Error", "Failed to get credential: ${e.localizedMessage}")
        }
        return null
    }

    private fun initializeCredentialManager(context: Context) {
        this.context = context
        this.credentialManager = CredentialManager.Companion.create(context)
    }

    suspend private fun buildCredentialRequest(): GetCredentialResponse {
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
            Log.d("Google ID Token", googleIdTokenCredential.idToken)
            return googleIdTokenCredential.idToken
        } else {
            Log.e("Error", "Credential is not of type Google ID!")
        }
        return null
    }
}