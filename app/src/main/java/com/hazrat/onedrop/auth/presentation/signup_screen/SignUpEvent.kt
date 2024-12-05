package com.hazrat.onedrop.auth.presentation.signup_screen

/**
 * @author Hazrat Ummar Shaikh
 * Created on 01-12-2024
 */

sealed interface SignUpEvent {
    data object SignUp : SignUpEvent

    data class SetName(val name: String): SignUpEvent

    data class SetEmail(val email: String): SignUpEvent

    data class SetPassword(val password: String): SignUpEvent

    data object TogglePasswordVisibility: SignUpEvent

    data object ClearName : SignUpEvent

    data object ClearEmail : SignUpEvent

}