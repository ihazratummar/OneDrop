package com.hazrat.onedrop.auth.presentation

import com.google.firebase.auth.FirebaseUser
import com.hazrat.onedrop.auth.domain.model.FirebaseUserData
import com.hazrat.onedrop.navigation.MasterRoot

data class AuthState(
    val isAuthenticated: Boolean = false,
    val firebaseUser: FirebaseUser? = null,
    val firebaseUserData: FirebaseUserData? = null,
    val errorMessage: String = "",
    val isLoading: Boolean = false
)
