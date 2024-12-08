package com.hazrat.onedrop.core.presentation.blood_donor_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.input.KeyboardType
import com.hazrat.onedrop.auth.presentation.common.CustomTextField
import com.hazrat.onedrop.core.domain.model.BloodDonorModel
import com.hazrat.onedrop.core.domain.model.BloodGroup
import com.hazrat.onedrop.core.presentation.component.BasicAppBar
import com.hazrat.onedrop.ui.theme.dimens
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.toSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.hazrat.onedrop.R
import com.hazrat.onedrop.core.domain.model.State


/**
 * @author Hazrat Ummar Shaikh
 * Created on 07-12-2024
 */

@Composable
fun CreateBloodDonorScreen(
    modifier: Modifier = Modifier,
    bloodDonorModel: BloodDonorModel,
    onBackClick: () -> Unit,
    bloodDonorEvent: (BloodDonorEvent) -> Unit,
    bloodDonorProfileState: BloodDonorProfileState
) {

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        var bloodDropDownCardSize by remember { mutableStateOf(Size.Zero) }
        var stateDropDownCardSize by remember { mutableStateOf(Size.Zero) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimens.size20)
        ) {
            Spacer(Modifier.height(dimens.size50))
            BasicAppBar(
                onBackClick = { onBackClick() },
                title = "Register as Blood Donor",
                action = {}
            )
            HorizontalDivider()
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Spacer(Modifier.height(dimens.size150))
                }
                item {
                    CustomTextField(
                        textFieldTopLabel = "Your Name",
                        value = bloodDonorModel.name,
                        onValueChange = { bloodDonorEvent(BloodDonorEvent.SetName(it)) },
                        keyboardType = KeyboardType.Text,
                        keyBoardActions = KeyboardActions.Default,
                        label = "Name",
                        placeholder = "User Name",
                        errorMessage = "",
                        isError = false
                    )
                    Spacer(Modifier.height(dimens.size10))
                    CustomTextField(
                        textFieldTopLabel = "Mobile Number",
                        value = bloodDonorModel.contactNumber,
                        onValueChange = { bloodDonorEvent(BloodDonorEvent.SetContactNumber(it)) },
                        keyboardType = KeyboardType.Number,
                        keyBoardActions = KeyboardActions.Default,
                        label = "Number",
                        placeholder = "Enter Number",
                        errorMessage = "",
                        isError = false
                    )
                    Spacer(Modifier.height(dimens.size10))
                    DropDownMenuCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinates ->
                                bloodDropDownCardSize = coordinates.size.toSize()
                            },
                        text = bloodDonorModel.bloodGroup.name,
                        onDropDownClick = { bloodDonorEvent(BloodDonorEvent.OnBloodDropDownClick) },
                        cardTopText = "Blood Group"
                    )
                    Spacer(Modifier.height(dimens.size10))
                    CustomTextField(
                        textFieldTopLabel = "District",
                        value = bloodDonorModel.district,
                        onValueChange = { bloodDonorEvent(BloodDonorEvent.SetDistrict(it)) },
                        keyboardType = KeyboardType.Text,
                        keyBoardActions = KeyboardActions.Default,
                        label = "District",
                        placeholder = "Enter District",
                        errorMessage = "",
                        isError = false
                    )

                    DropDownMenuCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinates ->
                                stateDropDownCardSize = coordinates.size.toSize()
                            },
                        text = bloodDonorModel.state.name,
                        onDropDownClick = { bloodDonorEvent(BloodDonorEvent.OnStateDropDownClick) }
                    )
                    Spacer(Modifier.height(dimens.size20))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onBackClick()
                            bloodDonorEvent(BloodDonorEvent.CreateBloodDonorProfile)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Become Donor",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }





                    GenericDropDownMenu(
                        modifier = Modifier.width(with(LocalDensity.current) { bloodDropDownCardSize.width.toDp() }),
                        isDropdownOpen = bloodDonorProfileState.isBloodDropDownOpen,
                        onDismiss = { bloodDonorEvent(BloodDonorEvent.OnBloodDropDownClick) },
                        items = BloodGroup.entries.toList(),
                        onItemClick = { bloodDonorEvent(BloodDonorEvent.SetBloodGroup(it)) },
                        itemLabel = { it.name }
                    )

                    GenericDropDownMenu(
                        modifier = Modifier.width(with(LocalDensity.current) { stateDropDownCardSize.width.toDp() }),
                        isDropdownOpen = bloodDonorProfileState.isStateDropDownOpen,
                        onDismiss = { bloodDonorEvent(BloodDonorEvent.OnStateDropDownClick) },
                        items = State.entries.toList(),
                        onItemClick = { bloodDonorEvent(BloodDonorEvent.SetState(it)) },
                        itemLabel = { it.name }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun DropDownMenuCard(
    modifier: Modifier = Modifier,
    text: String = "Blood Group",
    cardTopText: String = "",
    onDropDownClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = cardTopText)
            Spacer(Modifier.width(dimens.size20))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .combinedClickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = { onDropDownClick() }
                    ),
                border = BorderStroke(
                    width = dimens.size1, color = MaterialTheme.colorScheme.secondaryContainer
                ),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(dimens.size10)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimens.size15, horizontal = dimens.size15),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = text)
                    Icon(
                        painter = painterResource(R.drawable.arrow_down),
                        contentDescription = null
                    )
                }
            }
        }
    }
}


@Composable
fun <T> GenericDropDownMenu(
    modifier: Modifier = Modifier,
    isDropdownOpen: Boolean,
    onDismiss: () -> Unit,
    items: List<T>,
    onItemClick: (T) -> Unit,
    itemLabel: (T) -> String
) {
    DropdownMenu(
        expanded = isDropdownOpen,
        onDismissRequest = { onDismiss() },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        modifier = modifier.fillMaxWidth()
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                onClick = {
                    onDismiss()
                    onItemClick(item)
                },
                text = { Text(itemLabel(item)) }
            )
        }
    }
}