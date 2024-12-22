package com.hazrat.onedrop.auth.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.hazrat.onedrop.auth.data.repository.AuthRepositoryImpl
import com.hazrat.onedrop.auth.domain.repository.AuthRepository
import com.hazrat.onedrop.core.domain.repository.BloodDonorRepository
import com.hazrat.onedrop.util.datastore.AppDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 * Created on 01-12-2024
 */

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val bloodDonorRepository: BloodDonorRepository,
    private val firebaseAuth: FirebaseAuth,
    private val appDataStore: AppDataStore
) : ViewModel() {


    val authState: LiveData<AuthState> = authRepository.authState

    val profileState: StateFlow<ProfileState> = authRepository.profileState
    var userId = ""
    init {
        authRepository.checkAuthStatus()
        firebaseAuth.addAuthStateListener{auth ->
            val userIdAuth = auth.currentUser?.uid
            userId = userIdAuth.toString()
        }
    }


    fun event(event: AuthEvent) {
        when (event) {
            is AuthEvent.LoginWithGoogleCredential -> {
                loginWithGoogleCredential()
                viewModelScope.launch{
                    delay(2000)
                    bloodDonorRepository.isBloodDonorProfileExists(userId).collect{state ->
                        appDataStore.setBloodDonorRegistered(state)
                        Log.d("bloodDonorState", "refreshProfileState: $state")
                    }
                }
            }
            is AuthEvent.SetActivityContext -> setActivityContext(event.activity)
            AuthEvent.SignOut -> {
                viewModelScope.launch {
                    authRepository.signOut()
                    appDataStore.setBloodDonorRegistered(false)
                }
            }
        }
    }

    fun loginWithGoogleCredential() {
        viewModelScope.launch {
            authRepository.googleCredentialSignIn()
        }
    }

    fun setActivityContext(activity: Context) {
        (authRepository as? AuthRepositoryImpl)?.setActivity(activity)
    }
}