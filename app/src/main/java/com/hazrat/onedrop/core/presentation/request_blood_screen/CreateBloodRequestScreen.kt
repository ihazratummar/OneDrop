package com.hazrat.onedrop.core.presentation.request_blood_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * @author Hazrat Ummar Shaikh
 * Created on 08-12-2024
 */

@Composable
fun BloodRequestScreen(modifier: Modifier = Modifier) {

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Blood Request Screen")
        }
    }
}