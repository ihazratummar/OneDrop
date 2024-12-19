package com.hazrat.onedrop.core.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.hazrat.onedrop.auth.presentation.AuthEvent
import com.hazrat.onedrop.auth.presentation.ProfileState
import com.hazrat.onedrop.core.navigation.MainRoute.BloodDonorsProfileScreenRoute
import com.hazrat.onedrop.core.navigation.MainRoute.CreateBloodDonorProfileRoute
import com.hazrat.onedrop.core.navigation.MainRoute.CreateBloodRequestRoute
import com.hazrat.onedrop.core.navigation.MainRoute.EditProfileScreenRoute
import com.hazrat.onedrop.core.navigation.MainRoute.RequestBloodScreenRoute
import com.hazrat.onedrop.core.presentation.blood_donor_profile_details_screen.BloodDonorProfileScreen
import com.hazrat.onedrop.core.presentation.blood_donor_profile_details_screen.BloodDonorProfileViewModel
import com.hazrat.onedrop.core.presentation.blood_donor_screen.BloodDonorEvent
import com.hazrat.onedrop.core.presentation.blood_donor_screen.BloodDonorScreen
import com.hazrat.onedrop.core.presentation.blood_donor_screen.BloodDonorViewModel
import com.hazrat.onedrop.core.presentation.blood_donor_screen.CreateBloodDonorScreen
import com.hazrat.onedrop.core.presentation.home_screen.HomeScreen
import com.hazrat.onedrop.core.presentation.more_screen.MoreScreen
import com.hazrat.onedrop.core.presentation.request_blood_screen.BloodRequestScreen
import com.hazrat.onedrop.core.presentation.request_blood_screen.RequestBloodScreen
import com.hazrat.onedrop.core.presentation.self_profile_screen.EditProfileScreen
import com.hazrat.onedrop.core.presentation.self_profile_screen.SelfProfileScreen
import com.hazrat.onedrop.core.presentation.self_profile_screen.SelfProfileViewModel
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
    bloodDonorViewModel: BloodDonorViewModel,
    selfProfileViewModel: SelfProfileViewModel
) {
    navigation<RootNav>(startDestination = MainRoute.HomeRoute) {

        composable<MainRoute.HomeRoute> {
            val donorList by bloodDonorViewModel.allBloodDonorList.collectAsState()
            val bloodDonorEvent = bloodDonorViewModel::onEvent
            HomeScreen(
                onActivityClick = { route ->
                    navController.navigate(route.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    bloodDonorEvent(BloodDonorEvent.Refresh)
                },
                profileState = profileState,
                bloodDonorList = donorList
            )
        }
        composable<MainRoute.MoreRoute> {
            MoreScreen(
                authEvent = authEvent,
                clearAllState = {
                    bloodDonorViewModel.clearAllState()
                }
            )
        }


        composable<MainRoute.BloodDonorRoute> {
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
                snackBarHostState = snackbarHostState,
                onBloodDonorClick = {
                    navController.navigate(BloodDonorsProfileScreenRoute(it))
                },
                onGoToProfileClick = { navController.navigate(MainRoute.SelfProfileScreenRoute) }
            )
        }
        composable<CreateBloodDonorProfileRoute> {
            val bloodDonorModel by bloodDonorViewModel.bloodDonorModel.collectAsState()
            val bloodDonorProfileState by bloodDonorViewModel.bloodDonorProfileState.collectAsState()
            CreateBloodDonorScreen(
                onBackClick = { navController.popBackStack() },
                bloodDonorEvent = bloodDonorViewModel::onEvent,
                bloodDonorModel = bloodDonorModel,
                bloodDonorProfileState = bloodDonorProfileState,
                isEnable = bloodDonorProfileState.isFormValid
            )
        }
        composable<RequestBloodScreenRoute> {
            RequestBloodScreen(
                onFloatingButtonClick = { navController.navigate(CreateBloodRequestRoute) }
            )
        }


        composable<CreateBloodRequestRoute> {
            BloodRequestScreen()
        }

        composable<BloodDonorsProfileScreenRoute> { navBackStackEntry ->
            val bloodDonorProfileViewModel: BloodDonorProfileViewModel = hiltViewModel()
            val bloodDonorModel by bloodDonorProfileViewModel.bloodDonorModel.collectAsState()
            val userId = navBackStackEntry.toRoute<BloodDonorsProfileScreenRoute>().userId

            LaunchedEffect(userId) {
                userId.let {
                    bloodDonorProfileViewModel.getBloodDonorProfile(userId)
                }
            }
            BloodDonorProfileScreen(
                bloodDonorModel = bloodDonorModel,
                onBackClick = { navController.popBackStack() },
                profileEvent = bloodDonorProfileViewModel::onEvent
            )
        }

        composable<MainRoute.SelfProfileScreenRoute> {
            val bloodDonorModel by selfProfileViewModel.bloodDonorModel.collectAsState()
            val channelEvent by  selfProfileViewModel.events.collectAsState(initial = null)
            LaunchedEffect(Unit) {
                selfProfileViewModel.refresh()
            }
            SelfProfileScreen(
                bloodDonorModel = bloodDonorModel,
                onBackClick = { navController.popBackStack() },
                onActionClick = { navController.navigate(EditProfileScreenRoute) },
                channelEvent = channelEvent,
                snackBarHostState = snackbarHostState
            )
        }

        composable<EditProfileScreenRoute> {
            val bloodDonorModel by selfProfileViewModel.bloodDonorModel.collectAsState()
            val selfProfileState by selfProfileViewModel.selfProfileState.collectAsState()
            EditProfileScreen(
                bloodDonorModel = bloodDonorModel,
                selfBloodDonorEvent = selfProfileViewModel::onEvent,
                onBackClick = {
                    selfProfileViewModel.refresh()
                    navController.popBackStack()
                },
                isEnable = true,
                selfProfileState = selfProfileState
            )
        }
    }
}