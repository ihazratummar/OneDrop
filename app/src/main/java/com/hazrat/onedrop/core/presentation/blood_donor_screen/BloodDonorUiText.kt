package com.hazrat.onedrop.core.presentation.blood_donor_screen

import com.hazrat.onedrop.auth.util.asUiText
import com.hazrat.onedrop.util.UiText
import com.hazrat.onedrop.util.results.BloodDonorProfileError
import com.hazrat.onedrop.util.results.BloodDonorProfileSuccess
import com.hazrat.onedrop.util.results.Result
import com.hazrat.onedrop.util.results.SignInErrorResult
import com.hazrat.onedrop.util.results.SignInSuccessResult

/**
 * @author Hazrat Ummar Shaikh
 * Created on 07-12-2024
 */

fun BloodDonorProfileError.asUiText(): UiText {
    return when (this) {
        BloodDonorProfileError.FAILED -> {
            UiText.DynamicString("Failed to create Blood Donor Profile")
        }
        BloodDonorProfileError.UNKNOWN -> {
            UiText.DynamicString("Unknown Error")
        }
    }
}

fun Result.Error<*, BloodDonorProfileError>.asErrorUiText(): UiText {
    return error.asUiText()
}

fun BloodDonorProfileSuccess.asSuccessUiText(): UiText {
    return when (this) {
        BloodDonorProfileSuccess.SUCCESS -> {
            UiText.DynamicString("Blood Donor Profile Created Successfully")
        }
        BloodDonorProfileSuccess.PROFILE_EXISTS -> {
            UiText.DynamicString("Blood Donor Profile Already Exists")
        }
    }
}

fun Result.Success<BloodDonorProfileSuccess, *>.asSuccessUiText(): UiText {
    return data.asSuccessUiText()
}