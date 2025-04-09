package com.example.seabattle.data.auth

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat.getString
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.example.seabattle.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class GoogleSignIn(
    private val context: Context
) {
    private val credentialManager = CredentialManager.create(context)

    suspend fun signIn() {
        try {
            val result = buildCredentialRequest()
            handleSignIn(result)
        } catch (e: Exception) {
            Log.e("Error", "Failed to get credential: ${e.localizedMessage}")
        }
    }

    suspend private fun buildCredentialRequest(): GetCredentialResponse {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(context, R.string.default_web_client_id))
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return credentialManager.getCredential(context, request)
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        val credential = result.credential

        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.Companion.createFrom(credential.data)
            Log.d("Google ID Token", googleIdTokenCredential.idToken)

        } else {
            Log.e("Error", "Credential is not of type Google ID!")
        }
    }
}

