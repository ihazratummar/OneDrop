package com.hazrat.onedrop.core.presentation.blood_donor_screen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.hazrat.onedrop.core.domain.model.BloodDonorModel
import com.hazrat.onedrop.core.domain.repository.BloodDonorRepository
import com.hazrat.onedrop.util.datastore.AppDataStore
import com.hazrat.onedrop.util.datastore.DataStorePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 * Created on 06-12-2024
 */

@HiltViewModel
class BloodDonorViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: BloodDonorRepository,
    private val dataStorePreference: DataStorePreference,
    firebaseAuth: FirebaseAuth,
    private val appDataStore: AppDataStore
) : ViewModel() {

    private val _donorListWithoutCurrentUser = MutableStateFlow<List<BloodDonorModel>>(emptyList())
    val donorListWithoutCurrentUser: StateFlow<List<BloodDonorModel>> =
        _donorListWithoutCurrentUser.asStateFlow()



    private val _bloodDonorModel = MutableStateFlow(BloodDonorModel())
    val bloodDonorModel: StateFlow<BloodDonorModel> = _bloodDonorModel.asStateFlow()


    private val _bloodDonorProfileState =
        MutableStateFlow(
            BloodDonorScreenState(
                isBloodDonorProfileExists = appDataStore.isBloodDonorRegistered.stateIn(
                    viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000L),
                    initialValue = false
                ).value
            )
        )
    val bloodDonorProfileState: StateFlow<BloodDonorScreenState> = combine(
        _bloodDonorProfileState,
        appDataStore.isBloodDonorRegistered.stateIn(
            viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )
    ){state , isAccountAvailable ->
        state.copy(isBloodDonorProfileExists = isAccountAvailable)
    }.stateIn(
        viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = BloodDonorScreenState()
    )

    init {
        getListOfDonorsWithoutCurrentUser()
        refreshProfileState()
        firebaseAuth.addAuthStateListener { auth ->
            val userId = auth.currentUser?.uid
            _bloodDonorModel.value.userId = userId.toString()
        }
    }


    fun refreshProfileState() {
        viewModelScope.launch {
            delay(1000)
            repository.isBloodDonorProfileExists(userId = _bloodDonorModel.value.userId)
                .collect { state ->
                    appDataStore.setBloodDonorRegistered(state)
                    Log.d(
                        "bloodDonorState",
                        "refreshProfileState: $state ${_bloodDonorModel.value.userId}"
                    )
                }
        }
        getListOfDonorsWithoutCurrentUser()
    }



    fun getListOfDonorsWithoutCurrentUser() {
        viewModelScope.launch {
            repository.getListOfDonorsWithoutCurrentUser(
                userId = _bloodDonorModel.value.userId,
                bloodGroup = _bloodDonorProfileState.value.selectedBloodGroup,
                state = _bloodDonorProfileState.value.selectedState
            )
                .collect {
                    _donorListWithoutCurrentUser.value = it
                }
        }
    }

    fun onEvent(event: BloodDonorEvent) {
        when (event) {
            BloodDonorEvent.Refresh -> {
                viewModelScope.launch {
                    _bloodDonorProfileState.update { it.copy(isLoading = true) }
                    delay(1000)
                    _bloodDonorProfileState.update { it.copy(isLoading = false) }
                }
                refreshProfileState()
                getListOfDonorsWithoutCurrentUser()
            }


            BloodDonorEvent.ToggleBloodGroupFilter -> {
                _bloodDonorProfileState.update {
                    it.copy(
                        isBloodGroupFilterOpen = !it.isBloodGroupFilterOpen
                    )
                }
            }

            is BloodDonorEvent.SetBloodGroupFilter -> {
                if (event.bloodGroup == _bloodDonorProfileState.value.selectedBloodGroup) {

                    _bloodDonorProfileState.update {
                        it.copy(
                            selectedBloodGroup = null
                        )
                    }
                } else {
                    _bloodDonorProfileState.update {
                        it.copy(
                            selectedBloodGroup = event.bloodGroup
                        )
                    }
                }
                getListOfDonorsWithoutCurrentUser()
            }

            BloodDonorEvent.ToggleStateFilter -> {
                _bloodDonorProfileState.update {
                    it.copy(
                        isStateFilterOpen = !it.isStateFilterOpen
                    )
                }
            }

            is BloodDonorEvent.SetStateFilter -> {
                if (event.state == _bloodDonorProfileState.value.selectedState) {
                    _bloodDonorProfileState.update {
                        it.copy(
                            selectedState = null
                        )
                    }
                } else {
                    _bloodDonorProfileState.update {
                        it.copy(
                            selectedState = event.state
                        )
                    }
                }
                getListOfDonorsWithoutCurrentUser()
            }
        }
    }


    fun clearAllState() {
        // Resetting the blood donor profile to its default state
        _bloodDonorModel.value = BloodDonorModel()  // or initialize with defaults if necessary

        // Resetting the blood donor profile state
        _bloodDonorProfileState.value = BloodDonorScreenState(isBloodDonorProfileExists = false)

        // Resetting the lists
        _donorListWithoutCurrentUser.value = emptyList()

        // Resetting any other necessary properties as needed
        Log.d("bloodDonorState", "All states cleared.")
    }

}