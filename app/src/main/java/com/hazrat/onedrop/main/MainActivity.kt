package com.hazrat.onedrop.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.hazrat.onedrop.auth.presentation.AuthViewModel
import com.hazrat.onedrop.auth.presentation.common.NetworkStatusBar
import com.hazrat.onedrop.navigation.AppNavigation
import com.hazrat.onedrop.navigation.BottomNavigationBar
import com.hazrat.onedrop.ui.theme.OneDropTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
                    }
                ) {

                    val bottomPadding = it.calculateBottomPadding()
                    val authViewModel: AuthViewModel = hiltViewModel()
                    val authState = authViewModel.authState.collectAsState()

                    AppNavigation(
                        modifier = Modifier
                            .padding(bottom = bottomPadding),
                        navHostController = navController,
                        authState = authState.value,
                        snackbarHostState = snackBarHostState,
                    )

                }
            }
        }
    }
}



