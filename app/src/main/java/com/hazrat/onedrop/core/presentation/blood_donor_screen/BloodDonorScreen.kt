package com.hazrat.onedrop.core.presentation.blood_donor_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.hazrat.onedrop.core.domain.model.BloodDonorModel
import com.hazrat.onedrop.core.presentation.component.BasicAppBar
import com.hazrat.onedrop.core.presentation.component.BloodDonorsCards
import com.hazrat.onedrop.core.presentation.component.RegisterAsDonorCard
import com.hazrat.onedrop.ui.theme.dimens
import kotlinx.coroutines.launch

/**
 * @author Hazrat Ummar Shaikh
 * Created on 06-12-2024
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BloodDonorScreen(
    modifier: Modifier = Modifier,
    bloodDonorEvent: (BloodDonorEvent) -> Unit,
    donorList: List<BloodDonorModel>,
    bloodDonorProfileState: BloodDonorProfileState,
    onBackClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onGoToProfileClick: () -> Unit = {},
    bloodDonorChannelEvent: BloodDonorChannelEvent?,
    snackBarHostState: SnackbarHostState,
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(bloodDonorChannelEvent) {
        bloodDonorChannelEvent?.let {
            when (it) {
                is BloodDonorChannelEvent.Error -> {
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar(
                            message = it.error.asString(context),
                            duration = SnackbarDuration.Long,
                            withDismissAction = true
                        )
                    }
                }

                is BloodDonorChannelEvent.Success -> {
                    coroutineScope.launch {
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
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimens.size20)
        ) {
            Spacer(Modifier.height(dimens.size40))
            BasicAppBar(
                onBackClick = {
                    onBackClick()
                    bloodDonorEvent(BloodDonorEvent.Refresh)
                },
                title = "Blood Donors",
                action = {
                    Text(
                        text = "Refresh",
                        modifier = Modifier
                            .padding(dimens.size10)
                            .clickable {
                                bloodDonorEvent(BloodDonorEvent.Refresh)
                            },
                    )
                }
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    RegisterAsDonorCard(
                        onClick = {
                            if (!bloodDonorProfileState.isBloodDonorProfileExists) onRegisterClick() else onGoToProfileClick()
                        },
                        text = if (!bloodDonorProfileState.isBloodDonorProfileExists) "Register as Blood Donor" else "View Profile"
                    )
                }
                items(donorList) {
                    BloodDonorsCards(donorList = it)
                }
            }
        }
    }
}