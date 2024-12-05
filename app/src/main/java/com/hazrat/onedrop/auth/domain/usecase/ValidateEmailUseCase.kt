package com.hazrat.onedrop.auth.domain.usecase

import com.hazrat.onedrop.util.results.EmailValidationError
import com.hazrat.onedrop.util.results.EmailValidationSuccessResult
import com.hazrat.onedrop.util.results.Result

/**
 * @author Hazrat Ummar Shaikh
 * Created on 02-12-2024
 */

class ValidateEmailUseCase  {

    operator fun invoke(email: String) : Result<EmailValidationSuccessResult, EmailValidationError>{
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$")

        return if (email.isNotBlank() && emailRegex.matches(email)){
            Result.Success(EmailValidationSuccessResult.EMAIL_VALID)
        }else{
            Result.Error(EmailValidationError.INVALID_EMAIL)
        }
    }

}