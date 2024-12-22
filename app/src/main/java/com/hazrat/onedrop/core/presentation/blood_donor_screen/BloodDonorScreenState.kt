package com.hazrat.onedrop.core.presentation.blood_donor_screen

import com.hazrat.onedrop.core.domain.model.BloodGroup
import com.hazrat.onedrop.core.domain.model.State

data class BloodDonorScreenState(
    val isBloodDonorProfileExists: Boolean = false,
    val bloodGroup: BloodGroup? = null,
    val state: State? = null,
    val isStateFilterOpen : Boolean = false,
    val isBloodGroupFilterOpen : Boolean = false,
    val selectedBloodGroup : BloodGroup? = null,
    val selectedState : State? = null,
    val isLoading : Boolean = false,
)
