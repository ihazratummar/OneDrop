package com.hazrat.onedrop.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.hazrat.onedrop.auth.presentation.AuthState
import com.hazrat.onedrop.auth.presentation.AuthViewModel
import com.hazrat.onedrop.auth.presentation.common.NetworkStatusBar
import com.hazrat.onedrop.core.presentation.blood_donor_screen.BloodDonorViewModel
import com.hazrat.onedrop.navigation.AppNavigation
import com.hazrat.onedrop.navigation.BottomNavigationBar
import com.hazrat.onedrop.ui.theme.OneDropTheme
import com.hazrat.onedrop.util.datastore.DataStorePreference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dataStorePreference: DataStorePreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Hide the action bar
        actionBar?.hide()
        setContent {
            OneDropTheme {
                val navController = rememberNavController()
                val snackBarHostState = remember { SnackbarHostState() }
                val viewModel: MainViewModel = hiltViewModel()
                val isConnected = viewModel.isConnected.collectAsStateWithLifecycle()
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackBarHostState) { data ->
                            Snackbar(
                                modifier = Modifier,
                                snackbarData = data,
                                actionColor = MaterialTheme.colorScheme.primary,
                                shape = MaterialTheme.shapes.medium,
                                actionOnNewLine = false,
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        Column {
                            BottomNavigationBar(
                                navHostController = navController
                            )
                            NetworkStatusBar(
                                isConnectionAvailable = isConnected.value
                            )

                        }
                    },
                ) {
                    val bottomPadding = it.calculateBottomPadding()
                    val authViewModel: AuthViewModel = hiltViewModel()
                    val bloodDonorViewModel: BloodDonorViewModel = hiltViewModel()
                    val bloodDonorRegistered by bloodDonorViewModel.bloodDonorProfileState.collectAsState()
                    val authState = authViewModel.authState.observeAsState(initial = AuthState.Loading)
                    val profileState = authViewModel.profileState.collectAsState()
                    val authEvent = authViewModel::event
                    if (authState.value == AuthState.Loading || !isConnected.value) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        AppNavigation(
                            modifier = Modifier
                                .padding(bottom = bottomPadding),
                            navHostController = navController,
                            profileState = profileState.value,
                            snackbarHostState = snackBarHostState,
                            authState = authState.value,
                            authEvent = authEvent,
                            bloodDonorViewModel = bloodDonorViewModel
                        )
                    }
                }
            }
        }
    }
}



