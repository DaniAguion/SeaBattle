package com.example.seabattle.presentation.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.seabattle.R

enum class Tabs(
    val title: String,
    val textRef: Int,
    val icon: ImageVector
) {
    Home(
        Screen.Home.title,
        R.string.tab_home,
        Icons.Default.Home
    ),
    LeaderBoard(
        Screen.LeaderBoard.title,
        R.string.tab_leaderboard,
        Icons.Default.GridOn
    ),
    Profile(
        Screen.Profile.title,
        R.string.tab_profile,
        Icons.Default.AccountCircle
    )
}