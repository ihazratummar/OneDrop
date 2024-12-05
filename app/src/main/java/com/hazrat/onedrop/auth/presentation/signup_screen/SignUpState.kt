package com.hazrat.onedrop.auth.presentation.signup_screen

data class SignUpState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isFormValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val errorPassword: String? = null,
    val isLoading: Boolean = false,
    val errorEmail: String? = null,
)
