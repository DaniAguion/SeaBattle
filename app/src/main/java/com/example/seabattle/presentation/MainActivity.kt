package com.example.seabattle.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.seabattle.domain.usecase.presence.SetPresenceUseCase
import com.example.seabattle.presentation.screens.MyAppNavigation
import com.example.seabattle.presentation.theme.SeaBattleTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber


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

