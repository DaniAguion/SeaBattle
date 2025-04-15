package com.example.seabattle.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.seabattle.ui.battleplan.BattlePlanScreen
import com.example.seabattle.ui.home.HomeScreen
import com.example.seabattle.ui.profile.ProfileScreen
import com.example.seabattle.ui.welcome.WelcomeScreen
import com.example.seabattle.ui.theme.SeaBattleTheme
import com.example.seabattle.ui.splash.SplashScreen
import com.example.seabattle.ui.tabs.TabNavigation


enum class SeaBattleScreen(val title: String) {
    Splash(title = "Splash"),
    Welcome(title = "Welcome"),
    Home(title = "Home"),
    Profile(title = "Profile"),
    BattlePlan(title = "Battle Plan"),
    Play(title = "Play"),
    Gameboard(title = "Gameboard")
}

enum class TabItem(
    val title: String,
    val icon: ImageVector
) {
    Home(SeaBattleScreen.Home.title, Icons.Default.Home),
    BattlePlan(SeaBattleScreen.BattlePlan.title, Icons.Default.GridOn),
    Profile(SeaBattleScreen.Profile.title, Icons.Default.AccountCircle)
}

@Composable
fun SeaBattleApp(modifier : Modifier = Modifier) {
    val navController = rememberNavController()

    Scaffold(
        topBar = { SeaBattleTopBar() },
        bottomBar = { TabBar(navController) },
        modifier = Modifier.fillMaxSize(),
    ) {
        innerPadding ->

        NavHost(
            navController = navController,
            startDestination = SeaBattleScreen.Splash.title,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = SeaBattleScreen.Splash.title) {
                SplashScreen(navController = navController)
            }
            composable(route = SeaBattleScreen.Welcome.title) {
                WelcomeScreen()
            }
            composable(route = SeaBattleScreen.BattlePlan.title) {
                BattlePlanScreen()
            }
            composable(route = SeaBattleScreen.Home.title) {
                HomeScreen()
            }
            composable(route = SeaBattleScreen.Profile.title) {
                ProfileScreen()
            }
        }
    }
}

@Composable
fun TabBar(navController: NavHostController) {
    TabNavigation(
        modifier = Modifier,
        tabs = listOf(
            TabItem.BattlePlan,
            TabItem.Home,
            TabItem.Profile
        ),
        onTabSelected = { tabItem ->
            when (tabItem) {
                TabItem.BattlePlan -> navController.navigate(TabItem.BattlePlan.title)
                TabItem.Home -> navController.navigate(TabItem.Home.title)
                TabItem.Profile -> navController.navigate(TabItem.Profile.title)
            }
        },
        initialTab = TabItem.Home
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
        )
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SeaBattleTheme {
        SeaBattleApp()
    }
}