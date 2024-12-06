package com.hazrat.onedrop.auth.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.hazrat.onedrop.auth.data.repository.AuthRepositoryImpl
import com.hazrat.onedrop.auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {


    val authState: LiveData<AuthState> = authRepository.authState

    val profileState: StateFlow<ProfileState> = authRepository.profileState

    init {

        authRepository.checkAuthStatus()
    }


    fun event(event: AuthEvent) {
        when (event) {
            is AuthEvent.LoginWithGoogleCredential -> loginWithGoogleCredential()
            is AuthEvent.SetActivityContext -> setActivityContext(event.activity)
            AuthEvent.SignOut -> {
                viewModelScope.launch {
                    authRepository.signOut()
                }
            }
        }
    }

    fun loginWithGoogleCredential() {
        viewModelScope.launch {
            authRepository.googleCredentialSignIn().also { auth ->
            }
        }
    }

    fun setActivityContext(activity: Context) {
        (authRepository as? AuthRepositoryImpl)?.setActivity(activity)
    }
}