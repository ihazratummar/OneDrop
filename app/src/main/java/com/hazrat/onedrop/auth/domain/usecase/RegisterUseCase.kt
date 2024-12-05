package com.hazrat.onedrop.auth.domain.usecase

import com.hazrat.onedrop.auth.domain.repository.AuthRepository
import com.hazrat.onedrop.util.results.Result
import com.hazrat.onedrop.util.results.SignUpErrorResult
import com.hazrat.onedrop.util.results.SignUpSuccessResult
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 * Created on 02-12-2024
 */

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(
        email: String,
        password: String,
        name: String
    ): Result<SignUpSuccessResult, SignUpErrorResult> {
        return repository.signUp(email = email, password = password, name = name)
    }

}