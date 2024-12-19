package com.hazrat.onedrop.core.presentation.self_profile_screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.hazrat.onedrop.R
import com.hazrat.onedrop.core.domain.model.BloodDonorModel
import com.hazrat.onedrop.ui.theme.dimens
import com.hazrat.onedrop.util.event.ChannelEvent
import kotlinx.coroutines.launch

/**
 * @author Hazrat Ummar Shaikh
 * Created on 10-12-2024
 */

@Composable
fun SelfProfileScreen(
    modifier: Modifier = Modifier,
    bloodDonorModel: BloodDonorModel,
    onBackClick: () -> Unit,
    onActionClick: () -> Unit,
    channelEvent: ChannelEvent?,
    snackBarHostState: SnackbarHostState
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(channelEvent) {
        channelEvent?.let {
            when (it) {
                is ChannelEvent.Error -> {
                    coroutineScope.launch {
                        Log.d("SelfProfileScreen", "Error event received: ${it.error}")
                        snackBarHostState.showSnackbar(
                            message = it.error.asString(context),
                            duration = SnackbarDuration.Long,
                            withDismissAction = true
                        )
                    }
                }

                is ChannelEvent.Success -> {
                    coroutineScope.launch {
                        Log.d("SelfProfileScreen", "Error event received: ${it.success}")
                        snackBarHostState.showSnackbar(
                            message = it.success.asString(context),
                            duration = SnackbarDuration.Long,
                            withDismissAction = true
                        )
                    }
                }
            }
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopBarCard(
                modifier = Modifier,
                name = bloodDonorModel.name
            )
            Spacer(Modifier.height(dimens.size20))
            TabRowComponent(
                contentScreens = listOf(
                    { ProfileDetails(bloodDonorModel = bloodDonorModel) }
                ),
            )
        }
        ProfileTopBar(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = dimens.size50, start = dimens.size20, end = dimens.size20),
            onBackClick = {
                onBackClick()
            },
            onActionClick = { onActionClick() },
            actionIcon = painterResource(R.drawable.edit)
        )
    }
}


