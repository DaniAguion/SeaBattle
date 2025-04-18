package com.example.seabattle.presentation.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.seabattle.R
import com.example.seabattle.presentation.SeaBattleScreen
import org.koin.androidx.compose.koinViewModel


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    profileViewModel: ProfileViewModel = koinViewModel(),
) {
    val profileUiState by profileViewModel.uiState.collectAsState()

    LaunchedEffect(key1 = profileUiState.userLoggedIn) {
        if (!profileUiState.userLoggedIn) {
            navController.navigate(SeaBattleScreen.Welcome.title)
        }
    }
    ProfileScreenContent(
        modifier = modifier,
        profileUiState = profileUiState,
        onLogoutButtonClicked = profileViewModel::onLogoutButtonClicked
    )
}

@Composable
fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    profileUiState: ProfileUiState = ProfileUiState(),
    onLogoutButtonClicked: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_medium))
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Profile Screen",
            modifier = modifier
                .padding(dimensionResource(R.dimen.padding_medium))
        )
        profileUiState.userProfile?.photoUrl?.let {
            if (it.isNotEmpty()){
                AsyncImage(
                    model = profileUiState.userProfile.photoUrl,
                    contentDescription = "User Profile Picture"
                )
            }
        }
        Text(
            text = profileUiState.userProfile?.displayName ?: "No name",
        )
        Text(
            text = profileUiState.userProfile?.email ?: "No email",
        )

        Spacer(
            modifier = Modifier.height(dimensionResource(R.dimen.padding_medium))
        )
        Button(
            onClick = onLogoutButtonClicked,
            Modifier.widthIn(min = 250.dp)
        ) {
            Text("SignOut")
        }
    }
}


@Preview (showBackground = true)
@Composable
fun ProfilePreview(){
    ProfileScreenContent(
        modifier = Modifier
    )
}