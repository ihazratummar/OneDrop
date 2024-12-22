package com.hazrat.onedrop.core.presentation.blood_donor_screen

import com.hazrat.onedrop.core.domain.model.BloodGroup
import com.hazrat.onedrop.core.domain.model.Gender
import com.hazrat.onedrop.core.domain.model.State

/**
 * @author Hazrat Ummar Shaikh
 * Created on 06-12-2024
 */

sealed interface BloodDonorEvent {
    data object Refresh : BloodDonorEvent
    data object ToggleBloodGroupFilter : BloodDonorEvent
    data class SetBloodGroupFilter(val bloodGroup: BloodGroup) : BloodDonorEvent
    data object ToggleStateFilter : BloodDonorEvent
    data class SetStateFilter(val state: State) : BloodDonorEvent
}