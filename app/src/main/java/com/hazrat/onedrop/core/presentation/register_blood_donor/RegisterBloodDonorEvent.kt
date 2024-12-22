package com.hazrat.onedrop.core.presentation.register_blood_donor

import com.hazrat.onedrop.core.domain.model.BloodGroup
import com.hazrat.onedrop.core.domain.model.Gender
import com.hazrat.onedrop.core.domain.model.State
import com.hazrat.onedrop.core.presentation.blood_donor_screen.BloodDonorEvent

/**
 * @author Hazrat Ummar Shaikh
 * Created on 20-12-2024
 */

sealed interface RegisterBloodDonorEvent {

    data class SetName(val name: String) : RegisterBloodDonorEvent
    data class SetContactNumber(val contactNumber: String) : RegisterBloodDonorEvent
    data class SetAge(val age: String) : RegisterBloodDonorEvent
    data class SetBloodGroup(val bloodGroup: BloodGroup) : RegisterBloodDonorEvent
    data class SetGender(val gender: Gender) : RegisterBloodDonorEvent
    data class SetAvailable(val available: Boolean) : RegisterBloodDonorEvent
    data class SetCity(val city: String) : RegisterBloodDonorEvent
    data class SetDistrict(val district: String) : RegisterBloodDonorEvent
    data class SetState(val state: State) : RegisterBloodDonorEvent

    data object CreateBloodDonorProfile : RegisterBloodDonorEvent


    data object OnBloodDropDownClick : RegisterBloodDonorEvent
    data object OnGenderDropDownClick : RegisterBloodDonorEvent
    data object OnStateDropDownClick : RegisterBloodDonorEvent
    data object OnCityDropDownClick: RegisterBloodDonorEvent
    data object OnDistrictDropDownClick: RegisterBloodDonorEvent

}