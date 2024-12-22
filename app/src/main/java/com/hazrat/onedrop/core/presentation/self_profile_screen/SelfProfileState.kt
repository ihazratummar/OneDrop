package com.hazrat.onedrop.core.presentation.self_profile_screen

data class SelfProfileState(
    val isGenderDropDownOpen: Boolean = false,
    val isBloodDropDownOpen: Boolean = false,
    val isStateDropDownOpen: Boolean = false,
    val isFormValid: Boolean = false,
)
