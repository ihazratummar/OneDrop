package com.hazrat.onedrop.auth.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.hazrat.onedrop.auth.presentation.AuthViewModel
import com.hazrat.onedrop.auth.presentation.loginscreen.SignInScreen
import com.hazrat.onedrop.auth.presentation.loginscreen.SignInViewMode
import com.hazrat.onedrop.auth.presentation.signup_screen.SignUpScreen
import com.hazrat.onedrop.auth.presentation.signup_screen.SignUpViewModel
import com.hazrat.onedrop.core.presentation.blood_donor_screen.BloodDonorEvent
import com.hazrat.onedrop.navigation.MasterRoot.RootNav

/**
 * @author Hazrat Ummar Shaikh
 * Created on 01-12-2024
 */

fun NavGraphBuilder.authNavigation(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    refreshDonorProfile: () -> Unit,
) {
    navigation<RootNav>(startDestination = AuthRoute.SignInScreenRoute) {
        composable<AuthRoute.SignInScreenRoute> {
            val authViewModel: AuthViewModel = hiltViewModel()
            val signInViewModel: SignInViewMode = hiltViewModel()
            val signInState = signInViewModel.signState.collectAsState()
            val authEvent = authViewModel::event
            val userEvent = signInViewModel.events.collectAsState(initial = null)
            SignInScreen(
                onSignUpButtonClick = {
                    navController.navigate(AuthRoute.SignUpScreenRoute) {
                        popUpTo(AuthRoute.SignUpScreenRoute) {
                            inclusive = true
                        }
                    }
                },
                authEvent = authEvent,
                signInState = signInState.value,
                signInEvent = signInViewModel::onEvent,
                userEvent = userEvent.value,
                snackBarHostState = snackbarHostState,
                refreshDonorProfile = {
                    refreshDonorProfile()
                }
            )
        }
        composable<AuthRoute.SignUpScreenRoute> {
            val authViewModel: AuthViewModel = hiltViewModel()
            val signUpViewModel: SignUpViewModel = hiltViewModel()
            val authEvent = authViewModel::event
            val state = signUpViewModel.signUpState.collectAsState()
            val event = signUpViewModel::onEvent
            SignUpScreen(
                onSignInButtonClick = {
                    navController.navigate(AuthRoute.SignInScreenRoute) {
                        popUpTo(AuthRoute.SignInScreenRoute) {
                            inclusive = true
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                },
                authEvent = authEvent,
                signUpState = state.value,
                signUpEvent = event,
                refreshDonorProfile = {
                    refreshDonorProfile()
                }
            )
        }
    }

}