package com.hazrat.onedrop.auth.presentation.loginscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.onedrop.auth.domain.repository.AuthRepository
import com.hazrat.onedrop.auth.domain.usecase.ProfileUseCase
import com.hazrat.onedrop.auth.presentation.AuthState
import com.hazrat.onedrop.auth.util.asSuccessUiText
import com.hazrat.onedrop.auth.util.asUiText
import com.hazrat.onedrop.util.UiText
import com.hazrat.onedrop.util.results.PasswordValidationError
import com.hazrat.onedrop.util.results.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 * Created on 02-12-2024
 */

@HiltViewModel
class SignInViewMode @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    authRepository: AuthRepository
) : ViewModel() {


    private val _signInState = MutableStateFlow(SignInState())
    val signState: StateFlow<SignInState> = _signInState.asStateFlow()


    val authState: LiveData<AuthState> = authRepository.authState

    init {
        authRepository.checkAuthStatus()
    }


    private val eventChannel = Channel<UserEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.SetEmail -> {
                _signInState.update {
                    it.copy(
                        email = event.email
                    )
                }

                val emailResult = profileUseCase.emailValidationUseCase(_signInState.value.email)
                when (emailResult) {
                    is Result.Error -> {
                        _signInState.update {
                            it.copy(
                                errorEmail = "ex: sample@gmail.com",
                                isFormValid = false,
                                isEmailValid = false
                            )
                        }
                    }

                    is Result.Success -> {
                        _signInState.update {
                            it.copy(errorEmail = null, isFormValid = true, isEmailValid = true)
                        }
                    }
                }
                validateForm()
            }

            is SignInEvent.SetPassword -> {
                _signInState.update {
                    it.copy(
                        password = event.password
                    )
                }

                val passwordResult =
                    profileUseCase.passwordValidUseCase(_signInState.value.password)
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
                        _signInState.update {
                            it.copy(
                                isPasswordValid = false,
                                errorPassword = errorMessages
                            )
                        }
                    }

                    is Result.Success -> {
                        // Password is valid
                        _signInState.update {
                            it.copy(
                                isPasswordValid = true,
                                errorPassword = null // Clear any previous error
                            )
                        }
                    }
                }
                validateForm()


            }

            SignInEvent.SignIn -> {

                _signInState.update {
                    it.copy(
                        isLoading = true
                    )

                }
                viewModelScope.launch {
                    val result = profileUseCase.signInUseCase(
                        email = _signInState.value.email,
                        password = _signInState.value.password
                    )
                    when (result) {
                        is Result.Error -> {
                            _signInState.update { it.copy(isLoading = false) }

                            val errorMessage = result.error.asUiText()
                            eventChannel.send(UserEvent.Error(errorMessage))
                        }

                        is Result.Success -> {
                            val successMessage = result.data.asSuccessUiText()
                            eventChannel.send(UserEvent.Success(successMessage))
                            delay(2000L)
                            _signInState.update { it.copy(isLoading = false) }

                        }
                    }
                }

            }

            SignInEvent.TogglePasswordVisibility -> {
                _signInState.update {
                    it.copy(
                        isPasswordVisible = !it.isPasswordVisible
                    )
                }
            }

            SignInEvent.ClearEmail -> {
                _signInState.update {
                    it.copy(
                        email = ""
                    )
                }
            }
        }
    }


    private fun validateForm() {
        val email = _signInState.value.email
        val password = _signInState.value.password

        val formResult = profileUseCase.validateFormUseCase(email, password)

        when (formResult) {
            is Result.Error -> {
                val errorMessage = formResult.error.error.joinToString("\n")
                _signInState.update {
                    it.copy(
                        isFormValid = false,
                        errorPassword = errorMessage
                    )
                }
            }

            is Result.Success -> {
                _signInState.update {
                    it.copy(
                        isFormValid = true,
                        errorPassword = null
                    )
                }
            }
        }
    }
}


sealed interface UserEvent {
    data class Error(val error: UiText) : UserEvent
    data class Success(val success: UiText) : UserEvent
}