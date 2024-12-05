package com.hazrat.onedrop.auth.domain.usecase

import com.hazrat.onedrop.util.results.PasswordValidSuccessResult
import com.hazrat.onedrop.util.results.PasswordValidationError
import com.hazrat.onedrop.util.results.PasswordValidationErrors
import com.hazrat.onedrop.util.results.Result

/**
 * @author Hazrat Ummar Shaikh
 * Created on 02-12-2024
 */

class PasswordValidUseCase {

    operator fun invoke(password: String): Result<PasswordValidSuccessResult, PasswordValidationErrors> {

        val errors = mutableListOf<PasswordValidationError>()

        if (password.length < 8 || password.length > 16) {
            errors.add(PasswordValidationError.PASSWORD_LENGTH)
        }
        if (!password.any { it.isUpperCase() }){
            errors.add((PasswordValidationError.UPPERCASE_MISSING))
        }

        if (!password.any { it.isLowerCase() }){
            errors.add((PasswordValidationError.LOWERCASE_MISSING))
        }

        if (!password.any { it.isDigit() }){
            errors.add((PasswordValidationError.DIGIT_MISSING))
        }

        return if (errors.isEmpty()) {
            Result.Success(PasswordValidSuccessResult.PASSWORD_VALID)
        } else {
            Result.Error(PasswordValidationErrors(errors))
        }
    }

}