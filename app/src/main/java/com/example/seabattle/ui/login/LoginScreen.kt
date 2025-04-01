package com.example.seabattle.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seabattle.R
import org.koin.androidx.compose.koinViewModel


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = koinViewModel()
) {
    val loginUiState by loginViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_medium))
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(R.string.login_page_title),
            fontSize = 32.sp,
            style = MaterialTheme.typography.displayLarge
        )
        Text(
            text = stringResource(R.string.login_page_desc),
            fontSize = 20.sp,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small))
        )
        Spacer(
            modifier = Modifier.height(dimensionResource(R.dimen.padding_medium))
        )
        OutlinedTextField(
            value = loginUiState.email,
            onValueChange = {
                loginViewModel.onEmailUpdate(it)
            },
            label = { Text(stringResource(R.string.email)) },
            singleLine = true,
            /*
            isError = loginUiState.emailError.isNotEmpty(),
            supportingText = {
                Text(
                    text = loginUiState.emailError,
                    color = MaterialTheme.colorScheme.error
                )
            },
            */
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small)),

        )
        OutlinedTextField(
            value = loginUiState.password,
            onValueChange = { loginViewModel.onPasswordUpdate(it) },
            label = { Text(stringResource(R.string.password)) },
            singleLine = true,
            /*
            isError = loginUiState.passwordError.isNotEmpty(),
            supportingText = {
                Text(
                    text = loginUiState.passwordError,
                    color = MaterialTheme.colorScheme.error
                )
            },
            */
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small)),
        )
        Spacer(
            modifier = Modifier.height(dimensionResource(R.dimen.padding_small))
        )
        Button(
            onClick = { loginViewModel.onSignInButtonClicked() },
            Modifier.widthIn(min = 250.dp)
        ) {
            Text(stringResource(R.string.sign_in))
        }

    }
}


@Preview (showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        modifier = Modifier
    )
}