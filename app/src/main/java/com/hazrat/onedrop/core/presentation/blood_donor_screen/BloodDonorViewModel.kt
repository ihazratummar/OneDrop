package com.hazrat.onedrop.core.presentation.blood_donor_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.hazrat.onedrop.core.domain.model.BloodDonorModel
import com.hazrat.onedrop.core.domain.repository.BloodDonorRepository
import com.hazrat.onedrop.core.presentation.blood_donor_screen.BloodDonorChannelEvent.*
import com.hazrat.onedrop.util.UiText
import com.hazrat.onedrop.util.datastore.DataStorePreference
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
 * Created on 06-12-2024
 */

@HiltViewModel
class BloodDonorViewModel @Inject constructor(
    private val repository: BloodDonorRepository,
    private val dataStorePreference: DataStorePreference,
    firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _donorListWithoutCurrentUser = MutableStateFlow<List<BloodDonorModel>>(emptyList())
    val donorListWithoutCurrentUser: StateFlow<List<BloodDonorModel>> =
        _donorListWithoutCurrentUser.asStateFlow()

    private val _allBloodDonorList = MutableStateFlow<List<BloodDonorModel>>(emptyList())
    val allBloodDonorList: StateFlow<List<BloodDonorModel>> = _allBloodDonorList.asStateFlow()

    private val _bloodDonorProfile = MutableStateFlow(BloodDonorModel())
    val bloodDonorProfile: StateFlow<BloodDonorModel> = _bloodDonorProfile.asStateFlow()

    private val _bloodDonorProfileState =
        MutableStateFlow(BloodDonorProfileState(isBloodDonorProfileExists = dataStorePreference.getBloodDonorRegistered()))
    val bloodDonorProfileState: StateFlow<BloodDonorProfileState> =
        _bloodDonorProfileState.asStateFlow()

    private val eventChannel = Channel<BloodDonorChannelEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        getListOfDonorsWithoutCurrentUser()
        refreshProfileState()
        getListOfAllDonors()
        firebaseAuth.addAuthStateListener { auth ->
            val userId = auth.currentUser?.uid
            _bloodDonorProfile.value.userId = userId.toString()
        }
    }

    fun refreshProfileState() {
        viewModelScope.launch {
            delay(1000)
            repository.isBloodDonorProfileExists(userId = _bloodDonorProfile.value.userId).collect { state ->
                _bloodDonorProfileState.update {
                    it.copy(isBloodDonorProfileExists = state)
                }
                dataStorePreference.setBloodDonorRegistered(state)
                Log.d(
                    "bloodDonorState",
                    "refreshProfileState: $state ${_bloodDonorProfile.value.userId}"
                )
            }
        }
    }

    fun getListOfAllDonors() {
        viewModelScope.launch {
            repository.getListOfAllDonors().collect { donors ->
                _allBloodDonorList.value = donors
            }
        }
    }

    fun getListOfDonorsWithoutCurrentUser() {
        viewModelScope.launch {
            repository.getListOfDonorsWithoutCurrentUser(userId = _bloodDonorProfile.value.userId).collect {
                _donorListWithoutCurrentUser.value = it
            }
        }
    }

    fun onEvent(event: BloodDonorEvent) {
        when (event) {
            is BloodDonorEvent.SetName -> {
                _bloodDonorProfile.update {
                    it.copy(
                        name = event.name
                    )
                }
            }

            is BloodDonorEvent.SetBloodGroup -> {
                _bloodDonorProfile.update {
                    it.copy(
                        bloodGroup = event.bloodGroup
                    )
                }
            }

            is BloodDonorEvent.SetAvailable -> {
                _bloodDonorProfile.update {
                    it.copy(
                        available = event.available
                    )
                }
            }

            is BloodDonorEvent.SetContactNumber -> {
                _bloodDonorProfile.update {
                    it.copy(
                        contactNumber = event.contactNumber
                    )
                }
            }

            BloodDonorEvent.SetContactNumberPrivate -> {
                _bloodDonorProfile.update {
                    it.copy(
                        isContactNumberPrivate = !_bloodDonorProfile.value.isContactNumberPrivate
                    )
                }
            }

            BloodDonorEvent.SetNotificationEnabled -> {
                _bloodDonorProfile.update {
                    it.copy(
                        notificationEnabled = !_bloodDonorProfile.value.notificationEnabled
                    )
                }
            }

            BloodDonorEvent.SetNotificationScope -> TODO()

            is BloodDonorEvent.SetDistrict -> {
                _bloodDonorProfile.update {
                    it.copy(
                        district = event.district
                    )
                }
            }

            is BloodDonorEvent.SetState -> {
                _bloodDonorProfile.update {
                    it.copy(
                        state = event.state
                    )
                }
            }

            BloodDonorEvent.CreateBloodDonorProfile -> {
                viewModelScope.launch {
                    val result = repository.createBloodDonorProfile(
                        bloodDonorModel = BloodDonorModel(
                            userId =  _bloodDonorProfile.value.userId,
                            name = _bloodDonorProfile.value.name,
                            bloodGroup = _bloodDonorProfile.value.bloodGroup,
                            available = _bloodDonorProfile.value.available,
                            contactNumber =  _bloodDonorProfile.value.contactNumber,
                            district = _bloodDonorProfile.value.district,
                            isContactNumberPrivate = _bloodDonorProfile.value.isContactNumberPrivate,
                            notificationEnabled = _bloodDonorProfile.value.notificationEnabled,
                            notificationScope = _bloodDonorProfile.value.notificationScope,
                        )
                    )

                    when (result) {
                        is Result.Error -> {
                            val errorMessage = result.error.asUiText()
                            eventChannel.send(Error(errorMessage))
                        }

                        is Result.Success -> {
                            val successMessage = result.data.asSuccessUiText()
                            eventChannel.send(Success(successMessage))
                        }
                    }
                    refreshProfileState()
                    getListOfDonorsWithoutCurrentUser()
                    getListOfAllDonors()
                }
            }

            BloodDonorEvent.OnBloodDropDownClick -> {
                _bloodDonorProfileState.update {
                    it.copy(
                        isBloodDropDownOpen = !it.isBloodDropDownOpen
                    )
                }
            }

            BloodDonorEvent.OnStateDropDownClick -> {
                _bloodDonorProfileState.update {
                    it.copy(
                        isStateDropDownOpen = !it.isStateDropDownOpen
                    )
                }
            }

            BloodDonorEvent.Refresh -> {
                refreshProfileState()
                getListOfDonorsWithoutCurrentUser()
                getListOfAllDonors()
            }
        }
    }


    fun clearAllState() {
        // Resetting the blood donor profile to its default state
        _bloodDonorProfile.value = BloodDonorModel()  // or initialize with defaults if necessary

        // Resetting the blood donor profile state
        _bloodDonorProfileState.value = BloodDonorProfileState(isBloodDonorProfileExists = false)

        // Resetting the lists
        _donorListWithoutCurrentUser.value = emptyList()
        _allBloodDonorList.value = emptyList()

        // Resetting any other necessary properties as needed
        Log.d("bloodDonorState", "All states cleared.")
    }

}


sealed interface BloodDonorChannelEvent {
    data class Success(val success: UiText) : BloodDonorChannelEvent
    data class Error(val error: UiText) : BloodDonorChannelEvent
}