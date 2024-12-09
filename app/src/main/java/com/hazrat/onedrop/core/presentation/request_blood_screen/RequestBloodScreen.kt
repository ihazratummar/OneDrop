package com.hazrat.onedrop.core.presentation.request_blood_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hazrat.onedrop.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 * Created on 06-12-2024
 */

@Composable
fun RequestBloodScreen(
    modifier: Modifier = Modifier,
    onFloatingButtonClick: () -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(Modifier.height(dimens.size60))

        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = dimens.size30, bottom = dimens.size15),
            onClick = { onFloatingButtonClick() },
            content = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        )
    }

}