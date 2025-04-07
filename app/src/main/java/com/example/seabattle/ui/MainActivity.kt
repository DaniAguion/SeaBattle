package com.example.seabattle.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.seabattle.domain.auth.usecase.NewSessionUseCase
import com.example.seabattle.ui.theme.SeaBattleTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val homeViewModel: HomeViewModel by viewModel()

        setContent {
            SeaBattleTheme {
                SeaBattleApp()
            }
        }

    }
}