package com.hazrat.onedrop.core.presentation.blood_donor_screen

import com.hazrat.onedrop.core.domain.model.BloodGroup
import com.hazrat.onedrop.core.domain.model.State

/**
 * @author Hazrat Ummar Shaikh
 * Created on 06-12-2024
 */

sealed interface BloodDonorEvent {

    data class SetName(val name: String) : BloodDonorEvent
    data class SetBloodGroup(val bloodGroup: BloodGroup) : BloodDonorEvent
    data class SetAvailable(val available: Boolean) : BloodDonorEvent
    data class SetContactNumber(val contactNumber: String) : BloodDonorEvent
    data object SetContactNumberPrivate : BloodDonorEvent
    data object SetNotificationEnabled : BloodDonorEvent
    data object SetNotificationScope : BloodDonorEvent
    data class SetDistrict(val district: String) : BloodDonorEvent
    data class SetState(val state: State) : BloodDonorEvent

    data object CreateBloodDonorProfile : BloodDonorEvent

}