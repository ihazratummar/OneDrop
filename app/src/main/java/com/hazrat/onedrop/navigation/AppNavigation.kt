package com.hazrat.onedrop.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hazrat.onedrop.auth.navigation.authNavigation
import com.hazrat.onedrop.auth.presentation.AuthState
import com.hazrat.onedrop.core.navigation.BottomNavigation
import com.hazrat.onedrop.core.navigation.contentNavigation
import kotlinx.serialization.Serializable

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    authState: AuthState,
    snackbarHostState: SnackbarHostState
) {


    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = MasterRoot.RootNav
    ) {
        if (authState.isAuthenticated) {
            contentNavigation(
                navController = navHostController,
                snackbarHostState = snackbarHostState,
                authState = authState
            )
        } else {
            authNavigation(
                navController = navHostController,
                snackbarHostState = snackbarHostState,
            )
        }
    }
}

@Composable
fun BottomNavigationBar(
    navHostController: NavHostController
) {
    val bottomNavItems = listOf(
        BottomNavigation.HomeNav,
        BottomNavigation.SearchNav,
        BottomNavigation.MoreNav,
    )
    val currentStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = currentStackEntry?.destination
    val isBottomNavVisible =
        bottomNavItems.any { it.route::class.qualifiedName == currentDestination?.route }
    if (isBottomNavVisible) {
        Card(
            modifier = Modifier,
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            shape = RectangleShape
        ) {
            NavigationBar(
                modifier = Modifier,
                containerColor = Color.Transparent
            ) {
                bottomNavItems.forEach { item ->
                    val isSelected =
                        currentDestination?.hierarchy?.any { it.route == item.route::class.qualifiedName } == true
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navHostController.navigate(item.route) {
                                popUpTo(navHostController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                painter = if (isSelected) painterResource(id = item.fillIcon) else painterResource(
                                    id = item.icon
                                ),
                                contentDescription = item.name,
                            )
                        },
                        label = { Text(text = item.name) },
                        alwaysShowLabel = false,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = Color.Transparent,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        )
                    )
                }
            }
        }
    }
}

@Serializable
sealed class MasterRoot(){
    @Serializable
    data object RootNav : MasterRoot()

}
