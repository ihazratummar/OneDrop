package com.hazrat.onedrop.core.presentation.home_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hazrat.onedrop.auth.presentation.AuthEvent
import com.hazrat.onedrop.auth.presentation.ProfileState
import com.hazrat.onedrop.core.presentation.component.ActivityAs
import com.hazrat.onedrop.core.presentation.component.HomeActivityGrid
import com.hazrat.onedrop.core.presentation.component.HomePageHeaderCard
import com.hazrat.onedrop.core.presentation.component.HomePageSectionHeading
import com.hazrat.onedrop.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 */

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onActivityClick: (ActivityAs) -> Unit,
    authEvent: (AuthEvent) -> Unit,
    profileState: ProfileState
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                HomePageHeaderCard(
                    profileState = profileState
                )

            }
            item {
                Spacer(Modifier.height(dimens.size50))
                HomePageSectionHeading("Activity As")
                HomeActivityGrid(
                    onActivityClick = { route ->
                        onActivityClick(route)
                    }
                )
                Button(
                    onClick = { authEvent(AuthEvent.SignOut) }
                ) {
                    Text("Sign Out")
                }
            }
        }
    }
}



