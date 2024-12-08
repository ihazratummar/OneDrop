package com.hazrat.onedrop.auth.presentation.signup_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.hazrat.onedrop.auth.domain.repository.AuthRepository
import com.hazrat.onedrop.auth.domain.usecase.ProfileUseCase
import com.hazrat.onedrop.auth.presentation.AuthState
import com.hazrat.onedrop.core.domain.repository.BloodDonorRepository
import com.hazrat.onedrop.util.results.PasswordValidationError
import com.hazrat.onedrop.util.results.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 * Created on 01-12-2024
 */

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    authRepository: AuthRepository,
    private val bloodDonorRepository: BloodDonorRepository,
    firebaseAuth: FirebaseAuth
) : ViewModel() {


    var userId = ""
    init {
        authRepository.checkAuthStatus()
        firebaseAuth.addAuthStateListener{auth ->
            val userIdAuth = auth.currentUser?.uid
            userId = userIdAuth.toString()
        }
    }

    private val _signUpState = MutableStateFlow(
        SignUpState(
            name = "",
            email = "",
            password = "",
            isPasswordVisible = false,
            isFormValid = false,
            isPasswordValid = false
        )
    )

    val signUpState: StateFlow<SignUpState> = _signUpState.asStateFlow()


    fun onEvent(signUpEvent: SignUpEvent) {
        when (signUpEvent) {
            is SignUpEvent.SetEmail -> {
                _signUpState.update {
                    it.copy(
                        email = signUpEvent.email
                    )
                }

                val emailResult = profileUseCase.emailValidationUseCase(_signUpState.value.email)
                when (emailResult) {
                    is Result.Error -> {
                        _signUpState.update {
                            it.copy(errorPassword = "Invalid Email", isFormValid = false)
                        }
                    }

                    is Result.Success -> {
                        _signUpState.update {
                            it.copy(errorPassword = null, isFormValid = true)
                        }
                    }
                }
                validateForm()
            }

            is SignUpEvent.SetName -> {
                _signUpState.update {
                    it.copy(
                        name = signUpEvent.name
                    )
                }
                validateForm()
            }

            is SignUpEvent.SetPassword -> {
                _signUpState.update {
                    it.copy(
                        password = signUpEvent.password
                    )
                }

                val passwordResult =
                    profileUseCase.passwordValidUseCase(_signUpState.value.password)
                when (passwordResult) {
                    is Result.Error -> {
                        // Correctly handle errors wrapped in PasswordValidationErrors
                        val validationErrors = passwordResult.error
                        val errorMessages = validationErrors.errors.joinToString("\n") { error ->
                            when (error) {
                                PasswordValidationError.PASSWORD_LENGTH -> "Minimum 8 & Maximum 16 characters required"
                                PasswordValidationError.UPPERCASE_MISSING -> "Uppercase character missing"
                                PasswordValidationError.LOWERCASE_MISSING -> "Lowercase character missing"
                                PasswordValidationError.DIGIT_MISSING -> "Digit missing"
                                PasswordValidationError.PASSWORD_INVALID -> "Invalid Password"
                            }
                        }

                        // Update state with the error message
                        _signUpState.update {
                            it.copy(
                                isPasswordValid = false,
                                errorPassword = errorMessages
                            )
                        }
                    }

                    is Result.Success -> {
                        // Password is valid
                        _signUpState.update {
                            it.copy(
                                isPasswordValid = true,
                                errorPassword = null // Clear any previous error
                            )
                        }
                    }
                }
                validateForm()
            }

            SignUpEvent.TogglePasswordVisibility -> {
                _signUpState.update {
                    it.copy(
                        isPasswordVisible = !it.isPasswordVisible
                    )
                }
            }

            is SignUpEvent.SignUp -> {
                _signUpState.update { it.copy(isLoading = true) }
                viewModelScope.launch {
                    val result = profileUseCase.registerUseCase(
                        name = _signUpState.value.name,
                        email = _signUpState.value.email,
                        password = _signUpState.value.password
                    )

                    when (result) {
                        is Result.Error -> {
                            _signUpState.update { it.copy(isLoading = false) }
                        }
                        is Result.Success -> {
                            delay(2000L)
                            _signUpState.update { it.copy(isLoading = false) }
                            bloodDonorRepository.isBloodDonorProfileExists(userId)
                        }
                    }

                }
            }

            SignUpEvent.ClearEmail -> {
                _signUpState.update {
                    it.copy(
                        email = ""
                    )
                }
            }

            SignUpEvent.ClearName -> {
                _signUpState.update {
                    it.copy(
                        name = ""
                    )
                }
            }
        }
    }


    private fun validateForm() {
        val email = _signUpState.value.email
        val password = _signUpState.value.password

        val formResult = profileUseCase.validateFormUseCase(email, password)

        when (formResult) {
            is Result.Error -> {
                val errorMessage = formResult.error.error.joinToString("\n")
                _signUpState.update {
                    it.copy(
                        isFormValid = false,
                        errorPassword = errorMessage
                    )
                }
            }

            is Result.Success -> {
                _signUpState.update {
                    it.copy(
                        isFormValid = true,
                        errorPassword = null
                    )
                }
            }
        }
    }
}

