package com.hazrat.onedrop.core.presentation.self_profile_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.toSize
import com.hazrat.onedrop.R
import com.hazrat.onedrop.auth.presentation.common.CustomTextField
import com.hazrat.onedrop.core.domain.model.BloodDonorModel
import com.hazrat.onedrop.core.domain.model.BloodGroup
import com.hazrat.onedrop.core.domain.model.Gender
import com.hazrat.onedrop.core.domain.model.State
import com.hazrat.onedrop.core.presentation.blood_donor_screen.BloodDonorEvent
import com.hazrat.onedrop.core.presentation.blood_donor_screen.DropDownMenuCard
import com.hazrat.onedrop.core.presentation.blood_donor_screen.GenericDropDownMenu
import com.hazrat.onedrop.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 * Created on 10-12-2024
 */

@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    bloodDonorModel: BloodDonorModel,
    selfBloodDonorEvent: (SelfBloodDonorEvent) -> Unit,
    onBackClick:() -> Unit,
    isEnable: Boolean = false,
    selfProfileState: SelfProfileState
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        var bloodDropDownCardSize by remember { mutableStateOf(Size.Zero) }
        var stateDropDownCardSize by remember { mutableStateOf(Size.Zero) }
        var genderDropDownCardSize by remember { mutableStateOf(Size.Zero) }
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = dimens.size20)
        ) {
            Spacer(Modifier.height(dimens.size150))
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    CustomTextField(
                        textFieldTopLabel = "Your Name",
                        value = bloodDonorModel.name,
                        onValueChange = { selfBloodDonorEvent(SelfBloodDonorEvent.UpdateNameValue(it)) },
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
                        onValueChange = { selfBloodDonorEvent(SelfBloodDonorEvent.UpdateNumberValue(it)) },
                        keyboardType = KeyboardType.Number,
                        keyBoardActions = KeyboardActions.Default,
                        label = "Number",
                        placeholder = "Enter Number",
                        errorMessage = "",
                        isError = false
                    )
                    Spacer(Modifier.height(dimens.size10))
                    CustomTextField(
                        textFieldTopLabel = "Age",
                        value = bloodDonorModel.age.toString(),
                        onValueChange = { selfBloodDonorEvent(SelfBloodDonorEvent.UpdateAgeValue(it)) },
                        keyboardType = KeyboardType.Number,
                        keyBoardActions = KeyboardActions.Default,
                        label = "Age",
                        placeholder = "Enter Age",
                        errorMessage = "",
                        isError = false
                    )
                    Spacer(Modifier.height(dimens.size10))
                    DropDownMenuCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinates ->
                                genderDropDownCardSize = coordinates.size.toSize()
                            },
                        text = if (bloodDonorModel.gender != null) bloodDonorModel.gender.toString() else "Select Gender",
                        onDropDownClick = { selfBloodDonorEvent(SelfBloodDonorEvent.ToggleGenderDropDown) },
                        cardTopText = "Gender"
                    )
                    Spacer(Modifier.height(dimens.size10))
                    DropDownMenuCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinates ->
                                bloodDropDownCardSize = coordinates.size.toSize()
                            },
                        text = if (bloodDonorModel.bloodGroup != null) bloodDonorModel.bloodGroup.toString() else "Select Blood",
                        onDropDownClick = { selfBloodDonorEvent(SelfBloodDonorEvent.ToggleBloodGroupDropDown) },
                        cardTopText = "Blood Group"
                    )
                    Spacer(Modifier.height(dimens.size10))
                    CustomTextField(
                        textFieldTopLabel = "City",
                        value = bloodDonorModel.city,
                        onValueChange = { selfBloodDonorEvent(SelfBloodDonorEvent.UpdateCityValue(it)) },
                        keyboardType = KeyboardType.Text,
                        keyBoardActions = KeyboardActions.Default,
                        label = "City",
                        placeholder = "Your City?",
                        errorMessage = "",
                        isError = false
                    )
                    Spacer(Modifier.height(dimens.size10))
                    CustomTextField(
                        textFieldTopLabel = "District",
                        value = bloodDonorModel.district,
                        onValueChange = { selfBloodDonorEvent(SelfBloodDonorEvent.UpdateDistrictValue(it)) },
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
                        text = if (bloodDonorModel.state != null) bloodDonorModel.state.toString() else "Select State",
                        onDropDownClick = { selfBloodDonorEvent(SelfBloodDonorEvent.ToggleStateDropDown) }
                    )
                    Spacer(Modifier.height(dimens.size20))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onBackClick()
                            selfBloodDonorEvent(SelfBloodDonorEvent.UpdateBloodDonorProfile)
                            selfBloodDonorEvent(SelfBloodDonorEvent.Refresh)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        enabled = isEnable,
                        shape = RoundedCornerShape(dimens.size10)
                    ) {
                        Text(
                            text = "Update Profile",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }


                    GenericDropDownMenu(
                        modifier = Modifier
                            .fillMaxWidth()
                            .width(with(LocalDensity.current) { genderDropDownCardSize.width.toDp() }),
                        isDropdownOpen = selfProfileState.isGenderDropDownOpen,
                        onDismiss = { selfBloodDonorEvent(SelfBloodDonorEvent.ToggleGenderDropDown) },
                        items = Gender.entries.toList(),
                        onItemClick = { selfBloodDonorEvent(SelfBloodDonorEvent.UpdateGenderValue(it)) },
                        itemLabel = { it.toString() },
                    )

                    GenericDropDownMenu(
                        modifier = Modifier
                            .fillMaxWidth()
                            .width(with(LocalDensity.current) { bloodDropDownCardSize.width.toDp() }),
                        isDropdownOpen = selfProfileState.isBloodDropDownOpen,
                        onDismiss = { selfBloodDonorEvent(SelfBloodDonorEvent.ToggleBloodGroupDropDown) },
                        items = BloodGroup.entries.toList(),
                        onItemClick = { selfBloodDonorEvent(SelfBloodDonorEvent.UpdateBloodValue(it)) },
                        itemLabel = { it.toString() },
                    )

                    GenericDropDownMenu(
                        modifier = Modifier
                            .fillMaxWidth()
                            .width(with(LocalDensity.current) { stateDropDownCardSize.width.toDp() }),
                        isDropdownOpen = selfProfileState.isStateDropDownOpen,
                        onDismiss = { selfBloodDonorEvent(SelfBloodDonorEvent.ToggleStateDropDown) },
                        items = State.entries.toList(),
                        onItemClick = { selfBloodDonorEvent(SelfBloodDonorEvent.UpdateStateValue(it)) },
                        itemLabel = { it.toString() }
                    )
                }
            }
        }

        ProfileTopBar(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = dimens.size50, start = dimens.size20, end = dimens.size20),
            onBackClick = {onBackClick()},
            onActionClick = {
                onBackClick()
                selfBloodDonorEvent(SelfBloodDonorEvent.UpdateBloodDonorProfile)
                selfBloodDonorEvent(SelfBloodDonorEvent.Refresh)
            },
            actionIcon = painterResource(R.drawable.save)
        )
    }


}