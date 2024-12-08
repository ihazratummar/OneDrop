package com.hazrat.onedrop.core.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.hazrat.onedrop.auth.presentation.AuthEvent
import com.hazrat.onedrop.auth.presentation.ProfileState
import com.hazrat.onedrop.core.navigation.Route.CreateBloodDonorProfileRoute
import com.hazrat.onedrop.core.presentation.blood_donor_screen.BloodDonorScreen
import com.hazrat.onedrop.core.presentation.blood_donor_screen.BloodDonorViewModel
import com.hazrat.onedrop.core.presentation.blood_donor_screen.CreateBloodDonorScreen
import com.hazrat.onedrop.core.presentation.home_screen.HomeScreen
import com.hazrat.onedrop.core.presentation.more_screen.MoreScreen
import com.hazrat.onedrop.core.presentation.request_blood_screen.RequestBloodScreen
import com.hazrat.onedrop.navigation.MasterRoot.RootNav


/**
 * @author Hazrat Ummar Shaikh
 * Created on 01-12-2024
 */

fun NavGraphBuilder.contentNavigation(
    snackbarHostState: SnackbarHostState,
    profileState: ProfileState,
    authEvent: (AuthEvent) -> Unit,
    navController: NavController,
    bloodDonorViewModel: BloodDonorViewModel
) {
    navigation<RootNav>(startDestination = Route.HomeRoute) {

        composable<Route.HomeRoute> {
            val donorList by bloodDonorViewModel.allBloodDonorList.collectAsState()
            HomeScreen(
                onActivityClick = { route ->
                    navController.navigate(route.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                profileState = profileState,
                bloodDonorList = donorList
            )
        }
        composable<Route.MoreRoute> {
            MoreScreen(
                authEvent = authEvent,
                clearAllState = {
                    bloodDonorViewModel.clearAllState()
                }
            )
        }
        composable<Route.RequestBloodRoute> {
            RequestBloodScreen()
        }

        composable<Route.BloodDonorRoute> {
            val donorList by bloodDonorViewModel.donorListWithoutCurrentUser.collectAsState()
            val bloodDonorProfileState by bloodDonorViewModel.bloodDonorProfileState.collectAsState()
            val channelEvent by bloodDonorViewModel.events.collectAsState(initial = null)
            BloodDonorScreen(
                bloodDonorEvent = bloodDonorViewModel::onEvent,
                donorList = donorList,
                bloodDonorProfileState = bloodDonorProfileState,
                onBackClick = { navController.popBackStack() },
                onRegisterClick = { navController.navigate(CreateBloodDonorProfileRoute) },
                bloodDonorChannelEvent = channelEvent,
                snackBarHostState = snackbarHostState
            )
        }
        composable<CreateBloodDonorProfileRoute> {
            val bloodDonorModel by bloodDonorViewModel.bloodDonorProfile.collectAsState()
            val bloodDonorProfileState by bloodDonorViewModel.bloodDonorProfileState.collectAsState()
            CreateBloodDonorScreen(
                onBackClick = { navController.popBackStack() },
                bloodDonorEvent = bloodDonorViewModel::onEvent,
                bloodDonorModel = bloodDonorModel,
                bloodDonorProfileState = bloodDonorProfileState
            )

        }
    }
}