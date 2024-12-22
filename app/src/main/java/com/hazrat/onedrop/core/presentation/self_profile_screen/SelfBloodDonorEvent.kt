package com.hazrat.onedrop.core.presentation.self_profile_screen

/**
 * @author Hazrat Ummar Shaikh
 * Created on 10-12-2024
 */

sealed interface SelfBloodDonorEvent {
    data object Refresh : SelfBloodDonorEvent
    data object ToggleAvailable : SelfBloodDonorEvent

}