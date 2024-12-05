package com.hazrat.onedrop.auth.util

import com.hazrat.onedrop.util.UiText
import com.hazrat.onedrop.util.results.Result
import com.hazrat.onedrop.util.results.SignInErrorResult
import com.hazrat.onedrop.util.results.SignInSuccessResult

/**
 * @author Hazrat Ummar Shaikh
 * Created on 02-12-2024
 */

fun SignInErrorResult.asUiText(): UiText {
    return when (this) {
        SignInErrorResult.NO_INTERNET -> {
            UiText.DynamicString("No Internet")
        }

        SignInErrorResult.INVALID_EMAIL -> {
            UiText.DynamicString("Invalid Email")
        }

        SignInErrorResult.INVALID_PASSWORD -> {
            UiText.DynamicString("Invalid Password")
        }

        SignInErrorResult.UNKNOWN_ERROR -> {
            UiText.DynamicString("Unknown Error")
        }
    }
}

fun Result.Error<*, SignInErrorResult>.asErrorUiText(): UiText {
    return error.asUiText()
}

fun SignInSuccessResult.asSuccessUiText(): UiText {
    return when (this) {
        SignInSuccessResult.SIGN_IN_SUCCESS -> {
            UiText.DynamicString("Sign In Success")
        }

        SignInSuccessResult.USER_ALREADY_EXISTS -> {
            UiText.DynamicString("User Already Exists")
        }
    }
}

fun Result.Success<SignInSuccessResult, *>.asSuccessUiText(): UiText {
    return data.asSuccessUiText()
}