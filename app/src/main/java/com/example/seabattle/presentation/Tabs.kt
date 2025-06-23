package com.example.seabattle.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

enum class Tabs(
    val title: String,
    val icon: ImageVector
) {
    Home(Screen.Home.title, Icons.Default.Home),
    LeaderBoard(Screen.LeaderBoard.title, Icons.Default.GridOn),
    Profile(Screen.Profile.title, Icons.Default.AccountCircle)
}