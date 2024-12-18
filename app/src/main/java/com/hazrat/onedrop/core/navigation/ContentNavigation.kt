package com.hazrat.onedrop.core.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.hazrat.onedrop.auth.presentation.AuthState
import com.hazrat.onedrop.auth.presentation.AuthViewModel
import com.hazrat.onedrop.core.presentation.home_screen.HomeScreen
import com.hazrat.onedrop.navigation.MasterRoot.RootNav
import androidx.compose.runtime.getValue
import com.hazrat.onedrop.auth.presentation.ProfileState

/**
 * @author Hazrat Ummar Shaikh
 * Created on 01-12-2024
 */

fun NavGraphBuilder.contentNavigation(
    snackbarHostState: SnackbarHostState,
    profileState: ProfileState
) {
    navigation<RootNav>(startDestination = Route.HomeRoute) {
        composable<Route.HomeRoute> {
            val authViewModel: AuthViewModel = hiltViewModel()
            HomeScreen(
                onActivityClick = {},
                authEvent = authViewModel::event,
                profileState = profileState
            )
        }
    }
}