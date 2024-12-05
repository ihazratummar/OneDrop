package com.hazrat.onedrop.auth.presentation

import android.content.Context

/**
 * @author Hazrat Ummar Shaikh
 * Created on 01-12-2024
 */


sealed interface AuthEvent {

    data object LoginWithGoogleCredential : AuthEvent

    data class SetActivityContext(val activity: Context) : AuthEvent

    data object SignOut : AuthEvent
}