package com.hazrat.onedrop.core.presentation.blood_donor_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.toSize
import com.hazrat.onedrop.R
import com.hazrat.onedrop.core.domain.model.BloodDonorModel
import com.hazrat.onedrop.core.domain.model.BloodGroup
import com.hazrat.onedrop.core.domain.model.State
import com.hazrat.onedrop.core.presentation.component.BasicAppBar
import com.hazrat.onedrop.core.presentation.component.BloodDonorsCards
import com.hazrat.onedrop.core.presentation.component.CardLoadingAnimation
import com.hazrat.onedrop.core.presentation.component.RegisterAsDonorCard
import com.hazrat.onedrop.ui.theme.dimens
import kotlinx.coroutines.launch


/**
 * @author Hazrat Ummar Shaikh
 * Created on 06-12-2024
 */

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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
    onBloodDonorClick:(String) -> Unit
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var bloodDropDownCardSize by remember { mutableStateOf(Size.Zero) }
    var stateDownCardSize by remember { mutableStateOf(Size.Zero) }

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
            BasicAppBar(onBackClick = {
                onBackClick()
                bloodDonorEvent(BloodDonorEvent.Refresh)
            }, title = "Blood Donors", action = {
                Text(
                    text = "Refresh",
                    modifier = Modifier
                        .padding(dimens.size10)
                        .clickable {
                            bloodDonorEvent(BloodDonorEvent.Refresh)
                        },
                )
            })

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
                    if (donorList.isNotEmpty()) {
                        if (!bloodDonorProfileState.isLoading){
                            BloodDonorsCards(
                                donorList = it,
                                onClick = {
                                    onBloodDonorClick(it.userId)
                                }
                            )
                        }else{
                            CardLoadingAnimation()
                        }
                    }
                }
            }
        }


        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center
        ) {
            FilterDropDownMenu(
                modifier = Modifier

                    .onGloballyPositioned { coordinates ->
                        bloodDropDownCardSize = coordinates.size.toSize()
                    },
                onClick = { bloodDonorEvent(BloodDonorEvent.ToggleBloodGroupFilter) },
                text = if (bloodDonorProfileState.selectedBloodGroup != null)
                    bloodDonorProfileState.selectedBloodGroup.toString() else "Select Blood"
            )
            FilterDropDownMenu(
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        stateDownCardSize = coordinates.size.toSize()
                    },
                onClick = { bloodDonorEvent(BloodDonorEvent.ToggleStateFilter) },
                text = if (bloodDonorProfileState.selectedState != null)
                    bloodDonorProfileState.selectedState.toString() else "Select State"
            )
        }

        GenericDropDownMenu(
            modifier = Modifier
                .fillMaxWidth()
                .width(with(LocalDensity.current) { bloodDropDownCardSize.width.toDp() }),
            isDropdownOpen = bloodDonorProfileState.isBloodGroupFilterOpen,
            onDismiss = { bloodDonorEvent(BloodDonorEvent.ToggleBloodGroupFilter) },
            items = BloodGroup.entries.toList(),
            onItemClick = { bloodDonorEvent(BloodDonorEvent.SetBloodGroupFilter(it)) },
            itemLabel = { it.toString() }
        )

        GenericDropDownMenu(
            modifier = Modifier
                .fillMaxWidth()
                .width(with(LocalDensity.current) { stateDownCardSize.width.toDp() }),
            isDropdownOpen = bloodDonorProfileState.isStateFilterOpen,
            onDismiss = { bloodDonorEvent(BloodDonorEvent.ToggleStateFilter) },
            items = State.entries.toList(),
            onItemClick = { bloodDonorEvent(BloodDonorEvent.SetStateFilter(it)) },
            itemLabel = { it.toString() }
        )

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FilterDropDownMenu(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String = "Filter"

) {
    Card(
        modifier = modifier
            .navigationBarsPadding()
            .padding(dimens.size30)
            .combinedClickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    onClick()
                }
            ),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(
            width = dimens.size1, color = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(dimens.size10)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = dimens.size10),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(vertical = dimens.size10)
            )
            Icon(
                painter = painterResource(R.drawable.arrowup), contentDescription = null
            )
        }
    }
}