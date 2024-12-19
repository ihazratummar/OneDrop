package com.hazrat.onedrop.core.presentation.blood_donor_screen

import com.hazrat.onedrop.core.domain.model.BloodGroup
import com.hazrat.onedrop.core.domain.model.State

data class BloodDonorProfileState(
    val isLoading: Boolean = false,
    val isBloodDonorProfileExists: Boolean,
    val isRegisterDialogOpen: Boolean = false,
    val isBloodDropDownOpen: Boolean = false,
    val isGenderDropDownOpen: Boolean = false,
    val isStateDropDownOpen: Boolean = false,
    val isFormValid: Boolean = false,
    val isBloodGroupFilterOpen: Boolean = false,
    val selectedBloodGroup: BloodGroup? = null,
    val isStateFilterOpen: Boolean = false,
    val selectedState: State? = null
)
