package com.hazrat.onedrop.auth.domain.usecase

import com.hazrat.onedrop.auth.domain.repository.AuthRepository
import com.hazrat.onedrop.util.results.Result
import com.hazrat.onedrop.util.results.SignInErrorResult
import com.hazrat.onedrop.util.results.SignInSuccessResult
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 * Created on 02-12-2024
 */

class SignInUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<SignInSuccessResult, SignInErrorResult> {
        return repository.signIn(email = email, password)
    }
}