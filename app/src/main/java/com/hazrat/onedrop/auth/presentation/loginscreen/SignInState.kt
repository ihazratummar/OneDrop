package com.hazrat.onedrop.auth.presentation.loginscreen

data class SignInState(
    val email: String="",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorPassword: String? = null,
    val errorEmail: String? = null,
    val isFormValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isEmailValid: Boolean = false
)
