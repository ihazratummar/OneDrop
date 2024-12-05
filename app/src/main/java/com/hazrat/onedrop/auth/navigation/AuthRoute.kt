package com.hazrat.onedrop.auth.navigation

import kotlinx.serialization.Serializable

/**
 * @author Hazrat Ummar Shaikh
 * Created on 01-12-2024
 */

@Serializable
sealed class AuthRoute {

    @Serializable
    data object SignInScreenRoute : AuthRoute()

    @Serializable
    data object SignUpScreenRoute : AuthRoute()

    @Serializable
    data object ForgotPasswordScreenRoute : AuthRoute()

}