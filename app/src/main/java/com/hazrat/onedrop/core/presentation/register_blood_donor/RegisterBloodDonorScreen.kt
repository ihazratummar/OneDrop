package com.hazrat.onedrop.core.presentation.register_blood_donor

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.toSize
import com.hazrat.onedrop.R
import com.hazrat.onedrop.auth.presentation.common.CustomTextField
import com.hazrat.onedrop.core.domain.model.BloodGroup
import com.hazrat.onedrop.core.domain.model.Gender
import com.hazrat.onedrop.core.domain.model.State
import com.hazrat.onedrop.core.domain.model.cities_model.CitiesModelItem
import com.hazrat.onedrop.core.domain.model.district_model.DistrictListModelItem
import com.hazrat.onedrop.core.presentation.component.BasicAppBar
import com.hazrat.onedrop.ui.theme.dimens
import java.util.Locale


/**
 * @author Hazrat Ummar Shaikh
 * Created on 07-12-2024
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBloodDonorScreen(
    modifier: Modifier = Modifier,
    registerScreenState: BloodDonorProfileState,
    onBackClick: () -> Unit,
    registerEvents: (RegisterBloodDonorEvent) -> Unit,
    citiesModel: List<CitiesModelItem>,
    districts: List<DistrictListModelItem>
) {
    Box(
        modifier = modifier.fillMaxSize().navigationBarsPadding()
    ) {
        var bloodDropDownCardSize by remember { mutableStateOf(Size.Zero) }
        var stateDropDownCardSize by remember { mutableStateOf(Size.Zero) }
        var genderDropDownCardSize by remember { mutableStateOf(Size.Zero) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimens.size20)
        ) {
            BasicAppBar(
                onBackClick = { onBackClick() },
                title = if (registerScreenState.isBloodDonorProfileExists) "Update Profile" else "Register as Blood Donor",
                action = {}
            )
            HorizontalDivider()
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()

            ) {
                item {
                    CustomTextField(
                        textFieldTopLabel = "Your Name",
                        value = registerScreenState.name,
                        onValueChange = { registerEvents(RegisterBloodDonorEvent.SetName(it)) },
                        keyboardType = KeyboardType.Text,
                        keyBoardActions = KeyboardActions.Default,
                        label = "Name",
                        placeholder = "User Name",
                        errorMessage = "",
                        isError = false
                    )
                    Spacer(Modifier.height(dimens.size10))
                }
                item {
                    CustomTextField(
                        textFieldTopLabel = "Mobile Number",
                        value = registerScreenState.contactNumber,
                        onValueChange = { registerEvents(RegisterBloodDonorEvent.SetContactNumber(it)) },
                        keyboardType = KeyboardType.Number,
                        keyBoardActions = KeyboardActions.Default,
                        label = "Number",
                        placeholder = "Enter Number",
                        errorMessage = "",
                        isError = false
                    )
                    Spacer(Modifier.height(dimens.size10))
                }

                item {
                    CustomTextField(
                        textFieldTopLabel = "Age",
                        value = registerScreenState.age.toString(),
                        onValueChange = { registerEvents(RegisterBloodDonorEvent.SetAge(it)) },
                        keyboardType = KeyboardType.Number,
                        keyBoardActions = KeyboardActions.Default,
                        label = "Age",
                        placeholder = "Enter Age",
                        errorMessage = "",
                        isError = false
                    )
                    Spacer(Modifier.height(dimens.size10))
                }
                item {
                    Box(modifier = Modifier.fillMaxWidth()) {

                        DropDownMenuCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    genderDropDownCardSize = coordinates.size.toSize()
                                },
                            text = if (registerScreenState.gender != null) registerScreenState.gender.toString() else "Select Gender",
                            onDropDownClick = { registerEvents(RegisterBloodDonorEvent.OnGenderDropDownClick) },
                            cardTopText = "Gender"
                        )
                    }
                    GenericDropDownMenu(
                        modifier = Modifier
                            .fillMaxWidth()
                            .width(with(LocalDensity.current) { genderDropDownCardSize.width.toDp() }),
                        isDropdownOpen = registerScreenState.isGenderDropDownOpen,
                        onDismiss = { registerEvents(RegisterBloodDonorEvent.OnGenderDropDownClick) },
                        items = Gender.entries.toList(),
                        onItemClick = { registerEvents(RegisterBloodDonorEvent.SetGender(it)) },
                        itemLabel = { it.toString() },
                    )
                    Spacer(Modifier.height(dimens.size10))
                }
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DropDownMenuCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    bloodDropDownCardSize = coordinates.size.toSize()
                                },
                            text = if (registerScreenState.bloodGroup != null) registerScreenState.bloodGroup.toString() else "Select Blood",
                            onDropDownClick = { registerEvents(RegisterBloodDonorEvent.OnBloodDropDownClick) },
                            cardTopText = "Blood Group"
                        )
                        GenericDropDownMenu(
                            modifier = Modifier
                                .fillMaxWidth()
                                .width(with(LocalDensity.current) { bloodDropDownCardSize.width.toDp() }),
                            isDropdownOpen = registerScreenState.isBloodDropDownOpen,
                            onDismiss = { registerEvents(RegisterBloodDonorEvent.OnBloodDropDownClick) },
                            items = BloodGroup.entries.toList(),
                            onItemClick = { registerEvents(RegisterBloodDonorEvent.SetBloodGroup(it)) },
                            itemLabel = { it.toString() },
                        )
                    }
                    Spacer(Modifier.height(dimens.size10))
                }

                item {
                    DropDownMenuCard(
                        cardTopText = "City",
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = if (true) registerScreenState.city.toString()
                            .capitalize(Locale.ROOT) else "Select City",
                        onDropDownClick = { registerEvents(RegisterBloodDonorEvent.OnCityDropDownClick) }
                    )
                    if (registerScreenState.isCitySearching) {
                        CityModelBottomSheet(
                            registerEvents = registerEvents,
                            registerScreenState = registerScreenState,
                            citiesModel = citiesModel
                        )
                    }
                }

                item {
                    Spacer(Modifier.height(dimens.size10))

                    DropDownMenuCard(
                        cardTopText = "District",
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = if (true) registerScreenState.district.toString()
                            .capitalize(Locale.ROOT) else "Select District",
                        onDropDownClick = { registerEvents(RegisterBloodDonorEvent.OnDistrictDropDownClick) }
                    )
                    if (registerScreenState.isDistrictSearching) {
                        DistrictModelBottomSheet(
                            registerEvents = registerEvents,
                            registerScreenState = registerScreenState,
                            district = districts
                        )
                    }

                }
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        DropDownMenuCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    stateDropDownCardSize = coordinates.size.toSize()
                                },
                            text = if (registerScreenState.state != null) registerScreenState.state.toString() else "Select State",
                            onDropDownClick = { registerEvents(RegisterBloodDonorEvent.OnStateDropDownClick) }
                        )
                        GenericDropDownMenu(
                            modifier = Modifier
                                .fillMaxWidth()
                                .width(with(LocalDensity.current) { stateDropDownCardSize.width.toDp() }),
                            isDropdownOpen = registerScreenState.isStateDropDownOpen,
                            onDismiss = { registerEvents(RegisterBloodDonorEvent.OnStateDropDownClick) },
                            items = State.entries.toList(),
                            onItemClick = { registerEvents(RegisterBloodDonorEvent.SetState(it)) },
                            itemLabel = { it.toString() }
                        )
                    }
                    Spacer(Modifier.height(dimens.size20))
                }

            }
        }
        Button(
            modifier = Modifier
                .navigationBarsPadding()
                .imePadding()
                .fillMaxWidth()
                .align(
                    Alignment.BottomCenter
                ),
            onClick = {
                onBackClick()
                registerEvents(RegisterBloodDonorEvent.CreateBloodDonorProfile)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            enabled = registerScreenState.isFormValid,
            shape = RoundedCornerShape(dimens.size10)
        ) {
            Text(
                text = if (registerScreenState.isBloodDonorProfileExists)"Update" else "Become Donor",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CityModelBottomSheet(
    registerEvents: (RegisterBloodDonorEvent) -> Unit,
    registerScreenState: BloodDonorProfileState,
    citiesModel: List<CitiesModelItem>
) {
    ModalBottomSheet(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        onDismissRequest = {
            registerEvents(RegisterBloodDonorEvent.OnCityDropDownClick)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimens.size20)
        ) {
            OutlinedTextField(
                placeholder = { Text("Search City") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimens.size8),
                value = registerScreenState.city,
                onValueChange = {
                    registerEvents(RegisterBloodDonorEvent.SetCity(it))
                }
            )
            LazyColumn {
                items(citiesModel) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = dimens.size4),
                        onClick = {
                            registerEvents(RegisterBloodDonorEvent.SetCity(it.city))
                            registerEvents(RegisterBloodDonorEvent.OnCityDropDownClick)
                        },
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(dimens.size4)
                    ) {
                        Text(
                            modifier = Modifier.padding(
                                horizontal = dimens.size15,
                                vertical = dimens.size1
                            ),
                            text = it.city,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                }
            }
        }
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DistrictModelBottomSheet(
    registerEvents: (RegisterBloodDonorEvent) -> Unit,
    registerScreenState: BloodDonorProfileState,
    district: List<DistrictListModelItem>
) {
    ModalBottomSheet(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        onDismissRequest = {
            registerEvents(RegisterBloodDonorEvent.OnDistrictDropDownClick)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimens.size20)
        ) {
            OutlinedTextField(
                placeholder = { Text("Search District") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimens.size8),
                value = registerScreenState.district,
                onValueChange = {
                    registerEvents(RegisterBloodDonorEvent.SetDistrict(it))
                }
            )
            LazyColumn {
                items(district) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = dimens.size4),
                        onClick = {
                            registerEvents(RegisterBloodDonorEvent.SetDistrict(it.district))
                            registerEvents(RegisterBloodDonorEvent.OnDistrictDropDownClick)
                        },
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(dimens.size4)
                    ) {
                        Text(
                            modifier = Modifier.padding(
                                horizontal = dimens.size15,
                                vertical = dimens.size1
                            ),
                            text = it.district,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun DropDownMenuCard(
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
    selectedItem: T? = null,
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
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    onDismiss()
                    onItemClick(item)
                },
                text = {
                    Text(
                        text = itemLabel(item),
                        modifier = Modifier.fillMaxWidth(), // Ensures text takes full width
                        textAlign = TextAlign.Center, // Centers the text within the available space
                    )
                },
                colors = MenuDefaults.itemColors(
                    textColor = if (item == selectedItem) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    }
}