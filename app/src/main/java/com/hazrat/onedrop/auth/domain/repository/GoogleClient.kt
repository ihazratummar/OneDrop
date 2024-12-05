package com.hazrat.onedrop.auth.domain.repository

import android.app.Activity
import android.content.Context

/**
 * @author Hazrat Ummar Shaikh
 * Created on 01-12-2024
 */

interface GoogleClient {

    fun setActivity(activityContext: Context)

    fun isSignIn(): Boolean

    suspend fun signIn() : Boolean

    suspend fun signOut()

}