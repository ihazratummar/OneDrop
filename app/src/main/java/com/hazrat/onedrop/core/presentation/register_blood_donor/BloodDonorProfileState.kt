package com.hazrat.onedrop.core.presentation.register_blood_donor

import com.hazrat.onedrop.core.domain.model.BloodGroup
import com.hazrat.onedrop.core.domain.model.Gender
import com.hazrat.onedrop.core.domain.model.State

data class BloodDonorProfileState(

    /*
    Text Field States
     */

    val name: String = "",
    val age: String = "",
    val contactNumber: String = "",
    val district: String = "",
    val city: String = "",
    val bloodGroup: BloodGroup? =null,
    val state: State? = null,
    val gender: Gender? = null,

    /*
    DropDown State
     */

    val isBloodDropDownOpen: Boolean = false,
    val isGenderDropDownOpen: Boolean = false,
    val isStateDropDownOpen: Boolean = false,

    /*

     */
    val isBloodDonorProfileExists: Boolean = false,
    val isFormValid: Boolean = false,

    /*
    Searching State
     */

    val isCitySearching: Boolean = false,
    val isCityValid: Boolean = false,
    val isDistrictSearching: Boolean = false,
    val isDistrictValid: Boolean = false,
)
