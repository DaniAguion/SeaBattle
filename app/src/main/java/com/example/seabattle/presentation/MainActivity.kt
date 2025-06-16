package com.example.seabattle.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.seabattle.domain.usecase.presence.SetPresenceUseCase
import com.example.seabattle.presentation.theme.SeaBattleTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber


class MainActivity : ComponentActivity() {
    private val setPresenceUseCase: SetPresenceUseCase by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            setPresenceUseCase.invoke()
                .onFailure { error ->
                    Timber.e(error, "Failed to start presence management from MainActivity.")
                }
        }

        setContent {
            SeaBattleTheme {
                MainScreen()
            }
        }
    }
}

