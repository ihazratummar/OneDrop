package com.hazrat.onedrop.core.presentation.blood_donor_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.hazrat.onedrop.auth.presentation.common.CustomTextField
import com.hazrat.onedrop.core.domain.model.BloodDonorModel
import com.hazrat.onedrop.core.presentation.component.BasicAppBar
import com.hazrat.onedrop.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 * Created on 07-12-2024
 */

@Composable
fun CreateBloodDonorScreen(
    modifier: Modifier = Modifier,
    bloodDonorModel: BloodDonorModel?,
    onBackClick: () -> Unit,
    onRegisterClick: () -> Unit,
    bloodDonorEvent: (BloodDonorEvent) -> Unit
) {

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            BasicAppBar(
                onBackClick = { onBackClick() },
                title = "Register as Blood Donor",
                action = {}
            )
            Spacer(Modifier.height(dimens.size50))
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    CustomTextField(
                        textFieldTopLabel = "Your Name",
                        value = bloodDonorModel?.name ?: "",
                        onValueChange = { bloodDonorEvent(BloodDonorEvent.SetName(it)) },
                        keyboardType = KeyboardType.Text,
                        keyBoardActions = KeyboardActions.Default,
                        label = "Name",
                        placeholder = "User Name",
                        errorMessage = "",
                    )
                }
            }
        }
    }

}