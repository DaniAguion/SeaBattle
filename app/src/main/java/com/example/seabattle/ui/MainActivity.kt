package com.example.seabattle.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.seabattle.R
import com.example.seabattle.data.auth.GoogleSignIn
import com.example.seabattle.ui.theme.SeaBattleTheme
import com.example.seabattle.ui.welcome.AuthActivity
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val googleSignIn = GoogleSignIn(this)

        setContent {
            SeaBattleTheme {
                SeaBattleApp()
            }
            LaunchedEffect(Unit) {
                googleSignIn.signIn()
            }
        }
    }
}

