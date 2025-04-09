package com.example.seabattle.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.seabattle.ui.login.LoginActivity
import com.example.seabattle.ui.theme.SeaBattleTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SeaBattleTheme {
                SeaBattleApp()
            }
        }

        startActivity(Intent(this, LoginActivity::class.java))
    }
}