package com.hazrat.onedrop.auth.presentation.loginscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.hazrat.onedrop.R
import com.hazrat.onedrop.auth.presentation.AuthEvent
import com.hazrat.onedrop.auth.presentation.common.AuthenticationButton
import com.hazrat.onedrop.auth.presentation.common.BottomText
import com.hazrat.onedrop.auth.presentation.common.CustomTextField
import com.hazrat.onedrop.auth.presentation.common.SocialLoginButton
import com.hazrat.onedrop.main.MainActivity
import com.hazrat.onedrop.ui.theme.Nunito
import com.hazrat.onedrop.ui.theme.dimens
import kotlinx.coroutines.launch

/**
 * @author Hazrat Ummar Shaikh
 * Created on 30-11-2024
 */


@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    onSignUpButtonClick: () -> Unit,
    authEvent: (AuthEvent) -> Unit,
    signInState: SignInState,
    signInEvent: (SignInEvent) -> Unit,
    userEvent: UserEvent?,
    snackBarHostState: SnackbarHostState,
    refreshDonorProfile: () -> Unit
) {

    val context = LocalContext.current
    val activity = context as MainActivity
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(userEvent) {
        userEvent?.let {
            when (it) {
                is UserEvent.Error -> {
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar(
                            message = it.error.asString(context),
                            duration = SnackbarDuration.Long,
                            withDismissAction = true
                        )
                    }
                }

                is UserEvent.Success -> {
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar(
                            message = it.success.asString(context),
                            duration = SnackbarDuration.Long,
                            withDismissAction = true
                        )
                    }
                }
            }
        }
    }


    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimens.size20)
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item {
                Spacer(Modifier.height(dimens.size150))
                Text(
                    "Welcome To OneDrop",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Nunito
                )
                Spacer(Modifier.height(dimens.size50))
                CustomTextField(
                    value = signInState.email,
                    onValueChange = { signInEvent(SignInEvent.SetEmail(it)) },
                    textFieldTopLabel = "Email",
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                    label = "Type Email",
                    placeholder = "Enter Your Email!",
                    isError = signInState.email.isNotEmpty() && !signInState.isEmailValid,
                    keyBoardActions = KeyboardActions.Default,
                    errorMessage = signInState.errorEmail,
                    onTrailingIconCliCk = {
                        signInEvent(SignInEvent.ClearEmail)
                    }
                )
                Spacer(Modifier.height(dimens.size20))
                CustomTextField(
                    value = signInState.password,
                    onValueChange = { signInEvent(SignInEvent.SetPassword(it)) },
                    textFieldTopLabel = "Password",
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    label = "Type Password",
                    placeholder = "Enter Your Password!",
                    isError = signInState.password.isNotEmpty() && !signInState.isPasswordValid,
                    visualTransformation = if (signInState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    errorMessage = signInState.errorPassword,
                    keyBoardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            signInEvent(SignInEvent.SignIn)
                            refreshDonorProfile()
                        }
                    ),
                    isPasswordVisible = !signInState.isPasswordVisible,
                    onTrailingIconCliCk = {
                        signInEvent(SignInEvent.TogglePasswordVisibility)
                    },
                    isTrailingForPassword = true,
                )
            }
            item {
                Spacer(Modifier.height(dimens.size20))
                AuthenticationButton(
                    buttonText = "Login",
                    onButtonClick = {
                        keyboardController?.hide()
                        signInEvent(SignInEvent.SignIn)
                        refreshDonorProfile()
                    },
                    isButtonEnabled = signInState.isFormValid,
                    isLoadings = signInState.isLoading
                )
                Spacer(Modifier.height(dimens.size20))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SocialLoginButton(
                        buttonText = "Google",
                        icon = painterResource(R.drawable.google),
                        onButtonClick = {
                            authEvent(AuthEvent.SetActivityContext(activity))
                            authEvent(AuthEvent.LoginWithGoogleCredential)
                            refreshDonorProfile()
                        },
                        textColor = Color(0xffea4335)
                    )
                }
            }
            item {
                Spacer(Modifier.height(dimens.size40))
                BottomText(
                    messageText = "Don't have an account?",
                    buttonText = "Sign Up",
                    onButtonClick = {
                        onSignUpButtonClick()

                    }
                )
            }
        }
    }
}


