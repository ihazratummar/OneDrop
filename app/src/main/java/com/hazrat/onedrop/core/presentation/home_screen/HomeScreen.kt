package com.hazrat.onedrop.core.presentation.home_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hazrat.onedrop.auth.presentation.ProfileState
import com.hazrat.onedrop.core.domain.model.BloodDonorModel
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
    profileState: ProfileState,
    bloodDonorList: List<BloodDonorModel>,
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
                    },
                    bloodDonorList = bloodDonorList
                )
            }
        }
    }
}


