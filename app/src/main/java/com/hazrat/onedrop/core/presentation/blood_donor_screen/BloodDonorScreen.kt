package com.hazrat.onedrop.core.presentation.blood_donor_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.hazrat.onedrop.core.domain.model.BloodDonorModel
import com.hazrat.onedrop.core.presentation.component.BasicAppBar
import com.hazrat.onedrop.core.presentation.component.BloodDonorsCards
import com.hazrat.onedrop.core.presentation.component.RegisterAsDonorCard
import com.hazrat.onedrop.ui.theme.dimens
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog

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
    onRegisterClick: () -> Unit
) {
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
                },
                title = "Blood Donors",
                action = {
                    Text(text = "Filter", modifier = Modifier.padding(dimens.size10))
                }
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    RegisterAsDonorCard(
                        bloodDonorProfileState = bloodDonorProfileState,
                        onRegisterCardClick = {
                            onRegisterClick()
                        }
                    )
                }
                items(donorList) {
                    BloodDonorsCards(
                        donorList = it,

                        )
                }
            }
        }
    }
}