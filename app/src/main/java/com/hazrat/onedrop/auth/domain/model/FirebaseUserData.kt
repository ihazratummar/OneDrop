package com.hazrat.onedrop.auth.domain.model


/**
 * @author Hazrat Ummar Shaikh
 * Created on 01-12-2024
 */

data class FirebaseUserData(
    val userId: String?="",
    val fullName: String?="",
    val email: String? ="",
    val profilePictureUrl: String?=""
)
