package com.hazrat.onedrop.core.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class BloodDonorModel(
    var userId: String = "",  // Default value
    val name: String = "",  // Default value
    val bloodGroup: BloodGroup? = null,  // Assuming default blood group
    val city: String = "",  // Default value
    val district: String = "",  // Default value
    val state: State? = null,  // Assuming default state
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