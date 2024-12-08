package com.hazrat.onedrop.core.presentation.more_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hazrat.onedrop.auth.presentation.AuthEvent

/**
 * @author Hazrat Ummar Shaikh
 * Created on 06-12-2024
 */

@Composable
fun MoreScreen(
    modifier: Modifier = Modifier,
    authEvent: (AuthEvent) -> Unit,
    clearAllState: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                authEvent(AuthEvent.SignOut)
                clearAllState()
            }
        ) {
            Text("Sign Out")
        }
    }
}