package com.hazrat.onedrop.auth.presentation

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.hazrat.onedrop.auth.data.repository.GoogleClientImpl
import com.hazrat.onedrop.auth.domain.model.FirebaseUserData
import com.hazrat.onedrop.auth.domain.repository.AuthRepository
import com.hazrat.onedrop.auth.domain.repository.GoogleClient
import com.hazrat.onedrop.navigation.MasterRoot
import com.hazrat.onedrop.util.datastore.DataStorePreference
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
    private val googleAuthClient: GoogleClient,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    auth: FirebaseAuth,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()




    init {
        firebaseAuth.addAuthStateListener { auth ->
            auth.currentUser?.let { user ->
                fetchUserData(user)
            } ?: run {
                _authState.update { it.copy(isAuthenticated = false) }
            }
        }

    }


    private fun fetchUserData(firebaseUser: FirebaseUser) {
        val userId = firebaseUser.uid
        _authState.update { it.copy(isLoading = true) }

        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                val customUserData = document.toObject<FirebaseUserData>()
                if (customUserData != null) {
                    Log.d("AuthViewModel", "Firestore data retrieved: $customUserData")
                    _authState.update {
                        it.copy(
                            isAuthenticated = true,
                            firebaseUser = firebaseUser,
                            firebaseUserData = customUserData,
                            isLoading = false
                        )
                    }
                } else {
                    Log.e("AuthViewModel", "No document found for userId: $userId")
                    _authState.update {
                        it.copy(
                            isAuthenticated = true,
                            firebaseUser = firebaseUser,
                            firebaseUserData = null,
                            isLoading = false
                        )
                    }

                }
            }
            .addOnFailureListener { e ->
                Log.e("AuthViewModel", "Failed to fetch Firestore data: ${e.message}")
                _authState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to fetch Firestore data"
                    )
                }
            }
    }



    fun event(event: AuthEvent) {
        when (event) {
            is AuthEvent.LoginWithGoogleCredential -> loginWithGoogleCredential()
            is AuthEvent.SetActivityContext -> setActivityContext(event.activity)
            AuthEvent.SignOut -> {
                viewModelScope.launch {
                    googleAuthClient.signOut()
                    _authState.update {
                        it.copy(
                            isAuthenticated = false,
                            firebaseUser = null
                        )
                    }
                }
            }
        }
    }

    fun loginWithGoogleCredential() {
        viewModelScope.launch {
            googleAuthClient.signIn().also { auth ->
                _authState.update {
                    it.copy(
                        isAuthenticated = auth
                    )
                }
            }
        }
    }

    fun setActivityContext(activity: Context) {
        (googleAuthClient as? GoogleClientImpl)?.setActivity(activity)
    }
}