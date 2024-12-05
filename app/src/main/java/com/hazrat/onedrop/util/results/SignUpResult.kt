package com.hazrat.onedrop.util.results


/**
 * @author Hazrat Ummar Shaikh
 * Created on 02-12-2024
 */



enum class SignUpSuccessResult {
    SIGN_UP_SUCCESS,
    USER_ALREADY_EXISTS
}

enum class SignUpErrorResult : Error {
    NO_INTERNET,
    INVALID_EMAIL,
    INVALID_PASSWORD,
    USER_ALREADY_EXISTS,
    UNKNOWN_ERROR
}


enum class SignInSuccessResult {
    SIGN_IN_SUCCESS,
    USER_ALREADY_EXISTS
}

enum class SignInErrorResult : Error {
    NO_INTERNET,
    INVALID_EMAIL,
    INVALID_PASSWORD,
    UNKNOWN_ERROR
}

enum class PasswordValidSuccessResult {
    PASSWORD_VALID
}


data class PasswordValidationErrors(val errors: List<PasswordValidationError>) : Error

enum class PasswordValidationError : Error{
    PASSWORD_LENGTH,
    PASSWORD_INVALID,
    UPPERCASE_MISSING,
    LOWERCASE_MISSING,
    DIGIT_MISSING,
}

sealed class EmailValidationSuccessResult{
    object EMAIL_VALID : EmailValidationSuccessResult()
}

enum class EmailValidationError : Error {
    INVALID_EMAIL
}

data class FormValidationErrors(val error: List<String>): Error