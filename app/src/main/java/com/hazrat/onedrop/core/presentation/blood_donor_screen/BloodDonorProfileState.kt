package com.hazrat.onedrop.core.presentation.blood_donor_screen

data class BloodDonorProfileState(
    val isBloodDonorProfileExists: Boolean,
    val isRegisterDialogOpen: Boolean = false,
    val isBloodDropDownOpen: Boolean = false,
    val isStateDropDownOpen: Boolean = false
)
