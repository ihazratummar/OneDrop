package com.hazrat.onedrop.core.presentation.blood_donor_profile_details_screen

/**
 * @author Hazrat Ummar Shaikh
 * Created on 09-12-2024
 */

sealed interface BloodDonorProfileEvent {

    data object CallNow: BloodDonorProfileEvent

}