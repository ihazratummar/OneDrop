package com.hazrat.onedrop.auth.presentation.loginscreen

/**
 * @author Hazrat Ummar Shaikh
 * Created on 02-12-2024
 */

sealed interface SignInEvent {

    data object SignIn: SignInEvent

    data class SetEmail(val email: String): SignInEvent

    data class SetPassword(val password: String): SignInEvent

    data object TogglePasswordVisibility: SignInEvent

    data object ClearEmail: SignInEvent



}