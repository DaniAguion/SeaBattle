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
        setContent {
            SeaBattleTheme {
                SeaBattleApp()
            }
        }
    }
}