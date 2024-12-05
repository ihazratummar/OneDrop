package com.hazrat.onedrop.auth.domain.repository

import android.net.Uri
import com.hazrat.onedrop.util.results.Result
import com.hazrat.onedrop.util.results.SignInErrorResult
import com.hazrat.onedrop.util.results.SignInSuccessResult
import com.hazrat.onedrop.util.results.SignUpErrorResult
import com.hazrat.onedrop.util.results.SignUpSuccessResult

/**
 * @author Hazrat Ummar Shaikh
 * Created on 02-12-2024
 */

interface AuthRepository {

    suspend fun signUp(email:String, password:String, name: String): Result<SignUpSuccessResult, SignUpErrorResult>
    suspend fun signIn(email:String, password:String): Result<SignInSuccessResult, SignInErrorResult>


}