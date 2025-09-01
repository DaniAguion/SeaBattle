package com.example.seabattle.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalConfiguration
import android.content.res.Configuration
import androidx.compose.ui.semantics.clearAndSetSemantics
import com.example.seabattle.presentation.screens.leaderboard.LeaderBoardScreen
import com.example.seabattle.presentation.screens.game.GameScreen
import com.example.seabattle.presentation.screens.home.HomeScreen
import com.example.seabattle.presentation.screens.profile.ProfileScreen
import com.example.seabattle.presentation.screens.welcome.WelcomeScreen
import com.example.seabattle.presentation.screens.splash.SplashScreen



@Composable
fun MyAppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE


    Scaffold(
        topBar = {
            if (currentRoute == Screen.Game.title && isLandscape) {
                // Hide the top bar to improve the game experience in landscape mode
            } else {
                SeaBattleTopBar()
            }
        },
        bottomBar = {
            when (currentRoute) {
                Screen.LeaderBoard.title, Screen.Home.title, Screen.Profile.title -> {
                    TabBar(navController)
                }
                else -> {
                    // No tab bar for Splash or Welcome screens
                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
    ) {
        innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Splash.title,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screen.Splash.title) {
                SplashScreen(
                    navController = navController
                )
            }
            composable(route = Screen.Welcome.title) {
                WelcomeScreen(
                    navController = navController
                )
            }
            composable(route = Screen.LeaderBoard.title) {
                LeaderBoardScreen(
                    modifier = Modifier.fillMaxSize(),
                )
            }
            composable(route = Screen.Home.title) {
                HomeScreen(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController
                )
            }
            composable(route = Screen.Profile.title) {
                ProfileScreen(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController
                )
            }
            composable(route = Screen.Game.title) {
                GameScreen(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun TabBar(navController: NavHostController) {
    TabNavigation(
        modifier = Modifier,
        tabs = listOf(
            Tabs.LeaderBoard,
            Tabs.Home,
            Tabs.Profile
        ),
        onTabSelected = { tabItem ->
            when (tabItem) {
                Tabs.LeaderBoard -> navController.navigate(Tabs.LeaderBoard.title)
                Tabs.Home -> navController.navigate(Tabs.Home.title)
                Tabs.Profile -> navController.navigate(Tabs.Profile.title)
            }
        },
        initialTab = Tabs.Home
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeaBattleTopBar() {
    TopAppBar(
        title = { Text(text = "Sea Battle") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = Modifier.clearAndSetSemantics{}
    )
}
