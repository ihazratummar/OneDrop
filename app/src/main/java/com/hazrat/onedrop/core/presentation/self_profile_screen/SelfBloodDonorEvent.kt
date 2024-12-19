package com.hazrat.onedrop.core.presentation.self_profile_screen

import com.hazrat.onedrop.core.domain.model.BloodGroup
import com.hazrat.onedrop.core.domain.model.Gender
import com.hazrat.onedrop.core.domain.model.State

/**
 * @author Hazrat Ummar Shaikh
 * Created on 10-12-2024
 */

sealed interface SelfBloodDonorEvent {

    data class UpdateNameValue(val name: String) : SelfBloodDonorEvent
    data class UpdateAgeValue(val age: String) : SelfBloodDonorEvent
    data class UpdateNumberValue(val number: String) : SelfBloodDonorEvent
    data class UpdateBloodValue(val bloodGroup: BloodGroup): SelfBloodDonorEvent
    data class UpdateGenderValue(val gender: Gender): SelfBloodDonorEvent
    data class UpdateCityValue(val city: String): SelfBloodDonorEvent
    data class UpdateDistrictValue(val district: String): SelfBloodDonorEvent
    data class UpdateStateValue(val state: State): SelfBloodDonorEvent

    data object ToggleGenderDropDown : SelfBloodDonorEvent
    data object ToggleBloodGroupDropDown : SelfBloodDonorEvent
    data object ToggleStateDropDown : SelfBloodDonorEvent

    data object UpdateBloodDonorProfile : SelfBloodDonorEvent

    data object Refresh : SelfBloodDonorEvent

}