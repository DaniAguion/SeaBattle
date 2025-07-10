package com.example.seabattle.presentation.screens.welcome

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.seabattle.R
import com.example.seabattle.presentation.resources.toErrorMessageUI
import com.example.seabattle.presentation.screens.Tabs
import com.example.seabattle.presentation.theme.SeaBattleTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    welcomeViewModel: WelcomeViewModel = koinViewModel()
) {
    val welcomeUiState by welcomeViewModel.uiState.collectAsState()
    val context = LocalContext.current


    // Show a toast message when an error occurs
    LaunchedEffect(key1 = welcomeUiState.error) {
        welcomeUiState.error?.let { error ->
            Toast.makeText(context, context.getString(error.toErrorMessageUI()), Toast.LENGTH_LONG).show()
            welcomeViewModel.onErrorShown()
        }
    }

    // Navigate to Home screen if user is already logged in
    LaunchedEffect(key1 = welcomeUiState.isLoggedIn) {
        if (welcomeUiState.isLoggedIn) {
            navController.navigate(Tabs.Home.title)
        }
    }

    WelcomeScreenContent(
        modifier = modifier,
        welcomeUiState = welcomeUiState,
        onUsernameUpdate = welcomeViewModel::onUsernameUpdate,
        onEmailUpdate = welcomeViewModel::onEmailUpdate,
        onPasswordUpdate = welcomeViewModel::onPasswordUpdate,
        onLoginButtonClicked = welcomeViewModel::onLoginButtonClicked,
        onRegisterButtonClicked = welcomeViewModel::onRegisterButtonClicked,
        onGoogleButtonClicked = { welcomeViewModel.onGoogleButtonClicked(context) }
    )
}

@Composable
fun WelcomeScreenContent(
    modifier: Modifier = Modifier,
    welcomeUiState: WelcomeUiState = WelcomeUiState(),
    onUsernameUpdate: (String) -> Unit = {},
    onEmailUpdate: (String) -> Unit = {},
    onPasswordUpdate: (String) -> Unit = {},
    onLoginButtonClicked: () -> Unit = {},
    onRegisterButtonClicked: () -> Unit = {},
    onGoogleButtonClicked: () -> Unit = {},
) {
    val tabs = listOf(
        stringResource(R.string.sign_in),
        stringResource(R.string.sign_up)
    )
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Header
        item {
            Text(
                text = stringResource(R.string.welcome_page_title),
                fontSize = 32.sp,
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = stringResource(R.string.welcome_page_desc),
                fontSize = 20.sp,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small))
            )
        }

        //Forms to login and register
        item{
            HorizontalPager(state = pagerState) { page ->
                when (page) {
                    0 -> {
                        Column(
                            modifier = Modifier
                                .height(350.dp)
                                .padding(dimensionResource(R.dimen.padding_medium))
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            CommonForm(
                                welcomeUiState = welcomeUiState,
                                onEmailUpdate = onEmailUpdate,
                                onPasswordUpdate = onPasswordUpdate
                            )
                            Button(
                                onClick = onLoginButtonClicked,
                                Modifier.widthIn(min = 250.dp)
                            ) {
                                Text(stringResource(R.string.submit))
                            }
                        }
                    }

                    1 -> {
                        Column(
                            modifier = Modifier
                                .padding(dimensionResource(R.dimen.padding_medium))
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            OutlinedTextField(
                                value = welcomeUiState.username,
                                onValueChange = onUsernameUpdate,
                                label = { Text(stringResource(R.string.username)) },
                                singleLine = true,
                                isError = welcomeUiState.usernameError != null,
                                supportingText = {
                                    welcomeUiState.usernameError?.let {
                                        Text(
                                            text = stringResource(it.idString),
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                },
                                keyboardOptions = KeyboardOptions(
                                    autoCorrectEnabled = false,
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done
                                ),
                                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small)),
                            )
                            CommonForm(
                                welcomeUiState = welcomeUiState,
                                onEmailUpdate = onEmailUpdate,
                                onPasswordUpdate = onPasswordUpdate
                            )
                            Button(
                                onClick = onRegisterButtonClicked,
                                Modifier.widthIn(min = 250.dp)
                            ) {
                                Text(stringResource(R.string.submit))
                            }
                        }
                    }
                }
            }
            TabRow(selectedTabIndex = pagerState.currentPage) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(text = title) },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )
                }
            }
        }

        // Google button
        item {
            Button(
                onClick = onGoogleButtonClicked,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_big))
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = dimensionResource(R.dimen.padding_small)),
            ) {
                Text(stringResource(R.string.access_with_google))
            }
        }
    }
}


@Composable
fun CommonForm(
    welcomeUiState : WelcomeUiState,
    onEmailUpdate: (String) -> Unit,
    onPasswordUpdate: (String) -> Unit
){
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            value = welcomeUiState.email,
            onValueChange = onEmailUpdate,
            label = { Text(stringResource(R.string.email)) },
            singleLine = true,
            isError = welcomeUiState.emailError != null,
            supportingText = {
                welcomeUiState.emailError?.let {
                    Text(
                        text = stringResource(it.idString),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small)),

        )
        OutlinedTextField(
            value = welcomeUiState.password,
            onValueChange = onPasswordUpdate,
            label = { Text(stringResource(R.string.password)) },
            singleLine = true,
            isError = welcomeUiState.passwordError != null,
            supportingText = {
                welcomeUiState.passwordError?.let {
                    Text(
                        text = stringResource(it.idString),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small)),
        )
    }
    Spacer(
        modifier = Modifier.height(dimensionResource(R.dimen.padding_small))
    )
    welcomeUiState.msgResult?.let { msgResult ->
        when (msgResult) {
            InfoMessages.LOGIN_SUCCESSFUL -> {
                Text(
                    text = stringResource(R.string.login_successful),
                    color = Color.Green
                )
            }
            InfoMessages.LOGIN_UNSUCCESSFUL -> {
                Text(
                    text = stringResource(R.string.login_unsuccessful),
                    color = Color.Red
                )
            }
            InfoMessages.REGISTER_SUCCESSFUL -> {
                Text(
                    text = stringResource(R.string.register_successful),
                    color = Color.Green
                )
            }
            InfoMessages.REGISTER_UNSUCCESSFUL -> {
                Text(
                    text = stringResource(R.string.register_unsuccessful),
                    color = Color.Red
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    SeaBattleTheme {
        WelcomeScreenContent()
    }
}