package com.hazrat.onedrop.core.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class BloodDonorModel(
    val userId: String = "",  // Default value
    val name: String = "",  // Default value
    val bloodGroup: BloodGroup = BloodGroup.A_POSITIVE,  // Assuming default blood group
    val district: String = "",  // Default value
    val state: State = State.WEST_BENGAL,  // Assuming default state
    val available: Boolean = true,  // Default value
    val contactNumber: String = "",  // Default value
    val isContactNumberPrivate: Boolean = true,  // Default value
    val notificationEnabled: Boolean = true,  // Default value
    val notificationScope: Location = Location.DISTRICT  // Default value
)

@Serializable
enum class Location {
    DISTRICT,
    STATE
}