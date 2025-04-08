package com.example.seabattle.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.seabattle.ui.theme.SeaBattleTheme
import androidx.credentials.GetCredentialRequest
import com.example.seabattle.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //onStartSession()
        setContent {
            SeaBattleTheme {
                SeaBattleApp()
            }
        }
    }

    /*
    fun onStartSession() {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(BuildConfig.WEB_CLIENT_ID)
            .setFilterByAuthorizedAccounts(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        credentialManager.getCredential(request, this::handleSignIn)
    }
    private fun handleSignIn(result: GetCredentialResult) {
        when (result) {
            is GetCredentialResult.Success -> {
                val credential = result.credential
                // Handle successful sign-in with the credential
            }
            is GetCredentialResult.Failure -> {
                // Handle sign-in failure
            }
        }
    */
}