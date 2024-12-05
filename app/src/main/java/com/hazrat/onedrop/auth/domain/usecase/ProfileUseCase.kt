package com.hazrat.onedrop.auth.domain.usecase

data class ProfileUseCase(
    val passwordValidUseCase: PasswordValidUseCase,
    val registerUseCase: RegisterUseCase,
    val emailValidationUseCase: ValidateEmailUseCase,
    val validateFormUseCase: ValidateFormUseCase,
    val signInUseCase: SignInUseCase
)
