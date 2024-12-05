package com.hazrat.onedrop.auth.domain.usecase

import com.hazrat.onedrop.util.results.FormValidationErrors
import com.hazrat.onedrop.util.results.PasswordValidationError
import com.hazrat.onedrop.util.results.PasswordValidationErrors
import com.hazrat.onedrop.util.results.Result
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 * Created on 02-12-2024
 */

class ValidateFormUseCase @Inject constructor(
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: PasswordValidUseCase
) {

    operator fun invoke(email: String, password: String): Result<Unit, FormValidationErrors> {
        val emailResult = validateEmailUseCase(email)
        val passwordResult = validatePasswordUseCase(password)

        val errors = mutableListOf<String>()

        // Handle email validation errors
        if (emailResult is Result.Error) {
            errors.add("Invalid email address.")
        }

        // Handle password validation errors
        if (passwordResult is Result.Error) {
            val passwordErrors = passwordResult.error.errors // ✅ Correctly cast to PasswordValidationErrors
            passwordErrors.forEach { error ->
                when (error) {
                    PasswordValidationError.PASSWORD_LENGTH -> errors.add("Password must be between 8 and 16 characters.")
                    PasswordValidationError.UPPERCASE_MISSING -> errors.add("Password must contain at least one uppercase letter.")
                    PasswordValidationError.LOWERCASE_MISSING -> errors.add("Password must contain at least one lowercase letter.")
                    PasswordValidationError.DIGIT_MISSING -> errors.add("Password must contain at least one digit.")
                    PasswordValidationError.PASSWORD_INVALID -> errors.add("Password is invalid.")
                }
            }
        }

        // Return form validation result
        return if (errors.isEmpty()) {
            Result.Success(Unit) // Form is valid
        } else {
            Result.Error(FormValidationErrors(errors)) // Return all form errors
        }
    }

}