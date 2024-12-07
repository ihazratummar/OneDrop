package com.hazrat.onedrop.auth.presentation.signup_screen

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.hazrat.onedrop.main.MainActivity
import com.hazrat.onedrop.R
import com.hazrat.onedrop.auth.presentation.AuthEvent
import com.hazrat.onedrop.auth.presentation.common.AuthenticationButton
import com.hazrat.onedrop.auth.presentation.common.BottomText
import com.hazrat.onedrop.auth.presentation.common.CustomTextField
import com.hazrat.onedrop.auth.presentation.common.SocialLoginButton
import com.hazrat.onedrop.ui.theme.Nunito
import com.hazrat.onedrop.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 * Created on 30-11-2024
 */


@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onSignInButtonClick: () -> Unit,
    authEvent: (AuthEvent) -> Unit,
    signUpState: SignUpState,
    signUpEvent: (SignUpEvent) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val activity = context as MainActivity
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimens.size20)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item {
                Spacer(Modifier.height(dimens.size150))
                Text(
                    "Create Your Account",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Nunito
                )
                Spacer(Modifier.height(dimens.size50))

                CustomTextField(
                    value = signUpState.name,
                    onValueChange = { signUpEvent(SignUpEvent.SetName(it)) },
                    textFieldTopLabel = "Name",
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    label = "Name",
                    placeholder = "Enter Your Name!",
                    isError = false,
                    errorMessage = "",
                    keyBoardActions = KeyboardActions.Default,
                    onTrailingIconCliCk = {
                        signUpEvent(SignUpEvent.ClearName)
                    }
                )
                Spacer(Modifier.height(dimens.size20))
                CustomTextField(
                    value = signUpState.email,
                    onValueChange = { signUpEvent(SignUpEvent.SetEmail(it)) },
                    textFieldTopLabel = "Email",
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                    label = "Type Email",
                    placeholder = "Enter Your Email!",
                    isError = false,
                    errorMessage = signUpState.errorPassword,
                    keyBoardActions = KeyboardActions.Default,
                    onTrailingIconCliCk = {
                        signUpEvent(SignUpEvent.ClearEmail)
                    }
                )
                Spacer(Modifier.height(dimens.size20))
                CustomTextField(
                    value = signUpState.password,
                    onValueChange = { signUpEvent(SignUpEvent.SetPassword(it)) },
                    textFieldTopLabel = "Password",
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    label = "Type Password",
                    placeholder = "Enter Your Password!",
                    isError = signUpState.password.isNotEmpty() && !signUpState.isPasswordValid,
                    visualTransformation = if (signUpState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    errorMessage = signUpState.errorPassword,
                    keyBoardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            signUpEvent(SignUpEvent.SignUp)
                        }
                    ),
                    isTrailingForPassword = true,
                    onTrailingIconCliCk = {
                        signUpEvent(SignUpEvent.TogglePasswordVisibility)
                    },
                )
            }
            item {
                Spacer(Modifier.height(dimens.size20))
                AuthenticationButton(
                    modifier = Modifier.fillMaxWidth(),
                    buttonText = "Sign Up",
                    onButtonClick = {
                        keyboardController?.hide()
                        signUpEvent(SignUpEvent.SignUp)
                    },
                    isButtonEnabled = signUpState.isFormValid,
                    isLoadings = signUpState.isLoading
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
                        },
                        textColor = Color(0xffea4335)
                    )
                }
            }
            item {
                Spacer(Modifier.height(dimens.size40))
                BottomText(
                    messageText = "Already have an account?",
                    buttonText = "Login",
                    onButtonClick = { onSignInButtonClick() }
                )
            }
        }
    }
}


