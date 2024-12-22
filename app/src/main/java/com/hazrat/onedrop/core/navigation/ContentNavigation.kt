package com.hazrat.onedrop.core.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.hazrat.onedrop.core.navigation.MainRoute.RequestBloodScreenRoute
import com.hazrat.onedrop.core.navigation.MainRoute.SettingScreenRoute
import com.hazrat.onedrop.core.presentation.blood_donor_profile_details_screen.BloodDonorProfileScreen
import com.hazrat.onedrop.core.presentation.blood_donor_profile_details_screen.BloodDonorProfileViewModel
import com.hazrat.onedrop.core.presentation.blood_donor_screen.BloodDonorEvent
import com.hazrat.onedrop.core.presentation.blood_donor_screen.BloodDonorScreen
import com.hazrat.onedrop.core.presentation.blood_donor_screen.BloodDonorViewModel
import com.hazrat.onedrop.core.presentation.home_screen.HomeScreen
import com.hazrat.onedrop.core.presentation.home_screen.HomeViewModel
import com.hazrat.onedrop.core.presentation.more_screen.MoreScreen
import com.hazrat.onedrop.core.presentation.more_screen.MoreScreenViewModel
import com.hazrat.onedrop.core.presentation.more_screen.settings.SettingViewModel
import com.hazrat.onedrop.core.presentation.more_screen.settings.SettingsScreen
import com.hazrat.onedrop.core.presentation.register_blood_donor.CreateBloodDonorScreen
import com.hazrat.onedrop.core.presentation.register_blood_donor.RegisterBloodDonorViewModel
import com.hazrat.onedrop.core.presentation.request_blood_screen.BloodRequestScreen
import com.hazrat.onedrop.core.presentation.request_blood_screen.RequestBloodScreen
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
    bloodDonorViewModel: BloodDonorViewModel
) {
    navigation<RootNav>(startDestination = MainRoute.HomeRoute) {

        composable<MainRoute.HomeRoute> {
            val homeViewModel: HomeViewModel = hiltViewModel()
            val donorList by homeViewModel.allBloodDonorList.collectAsState()
            val homeState by homeViewModel.homeState.collectAsStateWithLifecycle()
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
                bloodDonorList = donorList,
                homeState = homeState
            )
        }
        composable<MainRoute.MoreRoute> {
            val bloodDonorViewModel: BloodDonorViewModel = hiltViewModel()
            val moreScreenViewModel: MoreScreenViewModel = hiltViewModel()
            val moreScreenState by moreScreenViewModel.moreScreenState.collectAsState()

            LaunchedEffect(Unit) {
                moreScreenViewModel.refresh()

            }
            MoreScreen(
                authEvent = authEvent,
                clearAllState = {
                    bloodDonorViewModel.clearAllState()
                },
                moreScreenState = moreScreenState,
                onProfileClick = {
                    if (!moreScreenState.isBloodDonorProfileExists) {
                        navController.navigate(CreateBloodDonorProfileRoute)
                    } else {
                        navController.navigate(MainRoute.SelfProfileScreenRoute)
                    }
                },
                onSettingsClick = {
                    navController.navigate(SettingScreenRoute)
                }
            )
        }

        composable<SettingScreenRoute> {
            val settingViewModel: SettingViewModel = hiltViewModel()
            val settingState by settingViewModel.settingState.collectAsState()
            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                settingScreenState = settingState,
                settingsEvent = settingViewModel::onEvent
            )
        }


        composable<MainRoute.BloodDonorRoute> {
            val registerViewModel: RegisterBloodDonorViewModel = hiltViewModel()
            val donorList by bloodDonorViewModel.donorListWithoutCurrentUser.collectAsStateWithLifecycle()
            val bloodDonorMode by bloodDonorViewModel.bloodDonorModel.collectAsStateWithLifecycle()
            val bloodDonorProfileState by bloodDonorViewModel.bloodDonorProfileState.collectAsStateWithLifecycle()
            val channelEvent by registerViewModel.events.collectAsStateWithLifecycle(initialValue = null)

            LaunchedEffect(Unit) {
                bloodDonorViewModel.refreshProfileState()
            }
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
                onGoToProfileClick = { navController.navigate(MainRoute.SelfProfileScreenRoute) },
                bloodDonorMode = bloodDonorMode
            )
        }
        composable<CreateBloodDonorProfileRoute> {
            val registerViewModel: RegisterBloodDonorViewModel = hiltViewModel()
            val registerScreenState by registerViewModel.registerBloodDonorState.collectAsState()
            val cityModel by registerViewModel.filteredCityModel.collectAsState()
            val districts by registerViewModel.districtModel.collectAsState()
            CreateBloodDonorScreen(
                onBackClick = { navController.popBackStack() },
                registerEvents = registerViewModel::onEvent,
                registerScreenState = registerScreenState,
                citiesModel = cityModel,
                districts = districts
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
            val selfProfileViewModel: SelfProfileViewModel = hiltViewModel()
            val bloodDonorModel by selfProfileViewModel.bloodDonorModel.collectAsState()
            val channelEvent by selfProfileViewModel.events.collectAsState(initial = null)
            LaunchedEffect(Unit) {
                selfProfileViewModel.refresh()
            }
            SelfProfileScreen(
                bloodDonorModel = bloodDonorModel,
                onBackClick = { navController.popBackStack() },
                onActionClick = { navController.navigate(CreateBloodDonorProfileRoute) },
                channelEvent = channelEvent,
                snackBarHostState = snackbarHostState,
                onEvent = selfProfileViewModel::onEvent
            )
        }

    }
}