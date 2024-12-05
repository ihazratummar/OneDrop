package com.hazrat.onedrop.auth.di

import com.hazrat.onedrop.auth.domain.repository.AuthRepository
import com.hazrat.onedrop.auth.domain.usecase.PasswordValidUseCase
import com.hazrat.onedrop.auth.domain.usecase.ProfileUseCase
import com.hazrat.onedrop.auth.domain.usecase.RegisterUseCase
import com.hazrat.onedrop.auth.domain.usecase.SignInUseCase
import com.hazrat.onedrop.auth.domain.usecase.ValidateEmailUseCase
import com.hazrat.onedrop.auth.domain.usecase.ValidateFormUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideRegisterUseCase(
        repository: AuthRepository
    ): RegisterUseCase {
        return RegisterUseCase(repository = repository)
    }

    @Provides
    fun providePasswordValidUseCase(): PasswordValidUseCase {
        return PasswordValidUseCase()

    }

    @Provides
    fun provideProfileUseCase(
        passwordValidUseCase: PasswordValidUseCase,
        registerUseCase: RegisterUseCase,
        emailValidationUseCase: ValidateEmailUseCase,
        validateFormUseCase: ValidateFormUseCase,
        signInUseCase: SignInUseCase
    ): ProfileUseCase {
        return ProfileUseCase(
            passwordValidUseCase = passwordValidUseCase,
            registerUseCase = registerUseCase,
            emailValidationUseCase = emailValidationUseCase,
            validateFormUseCase = validateFormUseCase,
            signInUseCase = signInUseCase
        )
    }

    @Provides
    fun provideEmailValidationUseCase(): ValidateEmailUseCase {
        return ValidateEmailUseCase()
    }

    @Provides
    fun provideValidateFormUseCase(
        validateEmailUseCase: ValidateEmailUseCase,
        validatePasswordUseCase: PasswordValidUseCase
    ): ValidateFormUseCase {
        return ValidateFormUseCase(
            validateEmailUseCase = validateEmailUseCase,
            validatePasswordUseCase = validatePasswordUseCase
        )
    }

    @Provides
    fun provideSignInUseCase(authRepository: AuthRepository): SignInUseCase {
        return SignInUseCase(authRepository)
    }
}
