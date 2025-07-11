package com.example.seabattle.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.seabattle.presentation.screens.MyAppNavigation
import com.example.seabattle.presentation.theme.SeaBattleTheme
import org.koin.android.ext.android.inject


class MainActivity : ComponentActivity() {
    private val soundManager: SoundManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SeaBattleTheme {
                MyAppNavigation()
            }
        }
    }


    // When the activity is destroyed, release the sound manager resources
    override fun onDestroy() {
        super.onDestroy()
        soundManager.release()
    }
}

