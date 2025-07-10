package com.example.seabattle.presentation.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.seabattle.R
import com.example.seabattle.domain.entity.User
import com.example.seabattle.presentation.screens.Screen
import com.example.seabattle.presentation.theme.SeaBattleTheme
import org.koin.androidx.compose.koinViewModel


@Composable
fun ProfileScreen(
    modifier: Modifier,
    navController: NavHostController,
    profileViewModel: ProfileViewModel = koinViewModel(),
) {
    val profileUiState by profileViewModel.uiState.collectAsState()

    // Stop listeners when the screen is disposed
    DisposableEffect(Unit) {
        onDispose {
            profileViewModel.stopListening()
        }
    }


    LaunchedEffect(key1 = profileUiState.userLoggedIn) {
        if (!profileUiState.userLoggedIn) {
            navController.navigate(Screen.Welcome.title)
        }
    }


    // Show a dialog when the user wants to delete a note permanently
    if (profileUiState.deleteDialog) {
        AlertDialog(
            onDismissRequest = { profileViewModel.onClickCancel() },
            title = { Text(stringResource(R.string.delete_account_title)) },
            text = { Text(stringResource(R.string.delete_account_title_msg)) },
            confirmButton = {
                Button(
                    onClick = {
                        profileViewModel.onClickConfirm()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                ) {
                    Text(stringResource(R.string.delete_button))
                }
            },
            dismissButton = {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            profileViewModel.onClickCancel()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Text(stringResource(R.string.cancel_button))
                    }
                }
            }
        )
    }


    ProfileScreenContent(
        modifier = modifier,
        user = profileUiState.user,
        onLogoutButtonClicked = profileViewModel::onLogoutButtonClicked,
        onDeleteAccountClicked = profileViewModel::onDeleteAccountClicked
    )
}

@Composable
fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    user: User,
    onLogoutButtonClicked: () -> Unit = {},
    onDeleteAccountClicked: () -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Header
        item {
            Text(
                text = "Profile Screen",
                fontSize = 32.sp,
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            )
        }

        // Profile Info
        item {
            if (user.photoUrl.isEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.account_box_40px),
                    contentDescription = "User photo",
                    modifier = Modifier.size(40.dp)
                )
            } else {
                AsyncImage(
                    model = user.photoUrl,
                    contentDescription = "User photo",
                    modifier = Modifier.size(40.dp),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.account_box_40px),
                )
            }

            Text(
                text = user.displayName,
            )
            Text(
                text = user.email,
            )
        }

        // Sign Out Button
        item {
            Button(
                onClick = onLogoutButtonClicked,
                modifier = Modifier
                    .widthIn(min = 200.dp)
                    .padding(dimensionResource(R.dimen.padding_big))
            ) {
                Text("SignOut")
            }
        }

        // Delete Account Button
        item {
            Button(
                onClick = onDeleteAccountClicked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
            ) {
                Text("Delete Account")
            }
        }
    }
}


@Preview (showBackground = true)
@Composable
fun ProfilePreview(){
    SeaBattleTheme {
        ProfileScreenContent(
            modifier = Modifier.fillMaxSize(),
            user = User(
                userId = "1",
                displayName = "John Doe",
                email = "@example.com",
                photoUrl = "",
                score = 100
            ),
            onLogoutButtonClicked = {}
        )
    }
}