package com.hazrat.onedrop.core.presentation.blood_donor_screen

import com.hazrat.onedrop.auth.util.asUiText
import com.hazrat.onedrop.util.UiText
import com.hazrat.onedrop.util.UiText.*
import com.hazrat.onedrop.util.results.BloodDonorProfileError
import com.hazrat.onedrop.util.results.BloodDonorProfileSuccess
import com.hazrat.onedrop.util.results.Result
import com.hazrat.onedrop.util.results.SelfProfileError
import com.hazrat.onedrop.util.results.SelfProfileSuccess
import com.hazrat.onedrop.util.results.SignInErrorResult
import com.hazrat.onedrop.util.results.SignInSuccessResult

/**
 * @author Hazrat Ummar Shaikh
 * Created on 07-12-2024
 */

fun BloodDonorProfileError.asUiText(): UiText {
    return when (this) {
        BloodDonorProfileError.FAILED -> {
            DynamicString("Failed to create Blood Donor Profile")
        }
        BloodDonorProfileError.UNKNOWN -> {
            DynamicString("Unknown Error")
        }
    }
}

fun Result.Error<*, BloodDonorProfileError>.bloodDonorErrorUiText(): UiText {
    return error.asUiText()
}

fun BloodDonorProfileSuccess.asSuccessUiText(): UiText {
    return when (this) {
        BloodDonorProfileSuccess.SUCCESS -> {
            DynamicString("Blood Donor Profile Created Successfully")
        }
        BloodDonorProfileSuccess.PROFILE_EXISTS -> {
            DynamicString("Blood Donor Profile Already Exists")
        }
    }
}

fun Result.Success<BloodDonorProfileSuccess, *>.bloodDonorSuccessUiText(): UiText {
    return data.asSuccessUiText()
}

/************
 * Self Profile Ui Text
 * **********/


fun SelfProfileSuccess.profileSuccessUiText(): UiText  {
    return when (this){
        SelfProfileSuccess.UPDATED -> {
            DynamicString("Blood Donor Profile Updated Successfully")
        }
    }
}

fun Result.Success<SelfProfileSuccess, *>.profileSuccessUiText(): UiText {
    return data.profileSuccessUiText()
}

fun SelfProfileError.profileErrorUiText(): UiText {
    return when (this) {
        SelfProfileError.UPDATE_FAILED -> {
            DynamicString("Failed to update Blood Donor Profile")
        }
    }
}

fun Result.Error<*, SelfProfileError>.profileErrorUiText(): UiText {
    return error.profileErrorUiText()
}