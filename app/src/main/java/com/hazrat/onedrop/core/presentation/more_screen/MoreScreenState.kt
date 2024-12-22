package com.hazrat.onedrop.core.presentation.more_screen

import com.hazrat.onedrop.core.domain.model.BloodGroup

data class MoreScreenState(
    val profileName: String? =null,
    val bloodGroup: BloodGroup? = null,
    val isBloodDonorProfileExists: Boolean = false
)
