package com.hazrat.onedrop.auth.presentation

import com.google.firebase.auth.FirebaseUser

data class ProfileState(
    val userData: UserData? = null,
)



data class UserData(
    val userId: String?="",
    val fullName: String?="",
    val email: String? =""
)
