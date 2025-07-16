package com.example.seabattle.presentation.screens.profile

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.seabattle.R
import com.example.seabattle.data.local.sampleUser
import com.example.seabattle.domain.entity.GameHistory
import com.example.seabattle.domain.entity.User
import com.example.seabattle.presentation.resources.toErrorMessageUI
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
    val context = LocalContext.current

    // Stop listeners when the screen is disposed
    DisposableEffect(Unit) {
        profileViewModel.startListening()
        profileViewModel.loadUserProfile()
        onDispose {
            profileViewModel.stopListening()
        }
    }


    // Show a toast message when an error occurs
    LaunchedEffect(key1 = profileUiState.error) {
        profileUiState.error?.let { error ->
            Toast.makeText(context, context.getString(error.toErrorMessageUI()), Toast.LENGTH_LONG).show()
            profileViewModel.onErrorShown()
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
        )
    }


    ProfileScreenContent(
        modifier = modifier,
        user = profileUiState.user,
        historyList = profileUiState.user.history.reversed(),
        errorList = profileUiState.errorList,
        loadingList = profileUiState.loadingList,
        onLogoutButtonClicked = profileViewModel::onLogoutButtonClicked,
        onDeleteAccountClicked = profileViewModel::onDeleteAccountClicked
    )
}

@Composable
fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    user: User,
    historyList: List<GameHistory> = emptyList(),
    errorList: Boolean,
    loadingList: Boolean,
    onLogoutButtonClicked: () -> Unit = {},
    onDeleteAccountClicked: () -> Unit = {}
) {
    var expandedOptions by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(top = 32.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,

    ) {
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
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.account_box_40px),
                    modifier = Modifier.size(40.dp)
                )
            }
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_medium)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
            ){
                Text(
                    text = user.displayName,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.titleMedium,
                )
            }

        }
        item {
            Button(
                onClick = onLogoutButtonClicked,
                modifier = Modifier
                    .widthIn(min = 200.dp)
                    .padding(bottom = dimensionResource(R.dimen.padding_medium)),
            ) {
                Text("SignOut")
            }
        }

        // Account Options Section
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedOptions = !expandedOptions },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.account_options),
                    fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                    textAlign = TextAlign.Center
                )
                Icon(
                    imageVector = if (expandedOptions) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expandedOptions) "Collapse" else "Expand"
                )
            }
            AnimatedVisibility(
                visible = expandedOptions,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Button(
                    onClick = onDeleteAccountClicked,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier
                        .padding(top = dimensionResource(R.dimen.padding_medium))
                ) {
                    Text("Delete Account")
                }
            }
        }

        // Played Games Title
        item{
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                elevation = CardDefaults.cardElevation(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxSize(),
                ){
                    Text(
                        text = stringResource(R.string.history_header),
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    HorizontalDivider(thickness = 1.dp)
                }
            }
        }


        // Played Games List
        when {
            loadingList ->
                item {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                }

            !errorList -> {
                items(items = historyList, key = { it.gameId }) { playedGame ->
                    PlayedGameCard(
                        userId = user.userId,
                        game = playedGame,
                        modifier = Modifier.fillMaxWidth()
                    )
                    HorizontalDivider(thickness = 1.dp)
                }
            }

            else -> {
                item {
                    Text(
                        text = stringResource(R.string.error_get_history),
                        modifier = Modifier.padding(8.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
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
            user = sampleUser,
            historyList = sampleUser.history,
            errorList = false,
            loadingList = false,
            onLogoutButtonClicked = {}
        )
    }
}