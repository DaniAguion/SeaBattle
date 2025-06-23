package com.example.seabattle.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.seabattle.presentation.screens.MyAppNavigation
import com.example.seabattle.presentation.theme.SeaBattleTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SeaBattleTheme {
                MyAppNavigation()
            }
        }
    }
}

