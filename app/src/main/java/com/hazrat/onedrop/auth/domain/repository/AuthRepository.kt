package com.hazrat.onedrop.auth.domain.repository

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import com.hazrat.onedrop.auth.presentation.AuthState
import com.hazrat.onedrop.auth.presentation.ProfileState
import com.hazrat.onedrop.util.results.Result
import com.hazrat.onedrop.util.results.SignInErrorResult
import com.hazrat.onedrop.util.results.SignInSuccessResult
import com.hazrat.onedrop.util.results.SignUpErrorResult
import com.hazrat.onedrop.util.results.SignUpSuccessResult
import kotlinx.coroutines.flow.StateFlow

/**
 * @author Hazrat Ummar Shaikh
 * Created on 02-12-2024
 */

interface AuthRepository {

    val profileState: StateFlow<ProfileState>
    val authState: LiveData<AuthState>



    suspend fun signUp(email:String, password:String, name: String): Result<SignUpSuccessResult, SignUpErrorResult>
    suspend fun signIn(email:String, password:String): Result<SignInSuccessResult, SignInErrorResult>
    suspend fun googleCredentialSignIn() : Boolean
    fun googleCredentialIsSignIn(): Boolean
    fun setActivity(activityContext: Context)
    suspend fun signOut()
    fun checkAuthStatus()
    fun fetchUserData()
}