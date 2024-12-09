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

    private val _bloodDonorModel = MutableStateFlow(BloodDonorModel())
    val bloodDonorModel: StateFlow<BloodDonorModel> = _bloodDonorModel.asStateFlow()

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
            _bloodDonorModel.value.userId = userId.toString()
        }
    }

    fun isFormValid() {
        val profile = _bloodDonorModel.value
        val isValid = profile.name.isNotBlank() &&
                profile.contactNumber.isNotBlank() &&
                profile.district.isNotBlank() &&
                profile.contactNumber.length == 10 &&
                profile.city.isNotBlank() &&
                profile.bloodGroup != null &&
                profile.state != null

        _bloodDonorProfileState.update {
            it.copy(isFormValid = isValid)
        }
    }


    fun refreshProfileState() {
        viewModelScope.launch {
            delay(1000)
            repository.isBloodDonorProfileExists(userId = _bloodDonorModel.value.userId)
                .collect { state ->
                    _bloodDonorProfileState.update {
                        it.copy(isBloodDonorProfileExists = state)
                    }
                    dataStorePreference.setBloodDonorRegistered(state)
                    Log.d(
                        "bloodDonorState",
                        "refreshProfileState: $state ${_bloodDonorModel.value.userId}"
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
            is BloodDonorEvent.SetName -> {
                _bloodDonorModel.update {
                    it.copy(
                        name = event.name
                    )
                }
                isFormValid()
            }

            is BloodDonorEvent.SetBloodGroup -> {
                _bloodDonorModel.update {
                    it.copy(
                        bloodGroup = event.bloodGroup
                    )
                }
                isFormValid()
            }

            is BloodDonorEvent.SetAvailable -> {
                _bloodDonorModel.update {
                    it.copy(
                        available = event.available
                    )
                }
            }

            is BloodDonorEvent.SetContactNumber -> {
                _bloodDonorModel.update {
                    it.copy(
                        contactNumber = event.contactNumber
                    )
                }
                isFormValid()
            }

            BloodDonorEvent.SetContactNumberPrivate -> {
                _bloodDonorModel.update {
                    it.copy(
                        isContactNumberPrivate = !_bloodDonorModel.value.isContactNumberPrivate
                    )
                }
                isFormValid()
            }

            BloodDonorEvent.SetNotificationEnabled -> {
                _bloodDonorModel.update {
                    it.copy(
                        notificationEnabled = !_bloodDonorModel.value.notificationEnabled
                    )
                }
            }

            BloodDonorEvent.SetNotificationScope -> TODO()

            is BloodDonorEvent.SetDistrict -> {
                _bloodDonorModel.update {
                    it.copy(
                        district = event.district
                    )
                }
                isFormValid()
            }

            is BloodDonorEvent.SetState -> {
                _bloodDonorModel.update {
                    it.copy(
                        state = event.state
                    )
                }
                isFormValid()
            }

            BloodDonorEvent.CreateBloodDonorProfile -> {
                viewModelScope.launch {
                    val result = repository.createBloodDonorProfile(
                        bloodDonorModel = BloodDonorModel(
                            userId = _bloodDonorModel.value.userId,
                            name = _bloodDonorModel.value.name,
                            bloodGroup = _bloodDonorModel.value.bloodGroup,
                            city = _bloodDonorModel.value.city,
                            available = _bloodDonorModel.value.available,
                            contactNumber = _bloodDonorModel.value.contactNumber,
                            district = _bloodDonorModel.value.district,
                            state = _bloodDonorModel.value.state,
                            isContactNumberPrivate = _bloodDonorModel.value.isContactNumberPrivate,
                            notificationEnabled = _bloodDonorModel.value.notificationEnabled,
                            notificationScope = _bloodDonorModel.value.notificationScope,
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
                viewModelScope.launch{
                    _bloodDonorProfileState.update { it.copy(isLoading = true) }
                    delay(1000)
                    _bloodDonorProfileState.update { it.copy(isLoading = false) }
                }
                refreshProfileState()
                getListOfDonorsWithoutCurrentUser()
                getListOfAllDonors()
            }

            is BloodDonorEvent.SetCity -> {
                _bloodDonorModel.update {
                    it.copy(
                        city = event.city
                    )
                }
            }

            BloodDonorEvent.ToggleBloodGroupFilter -> {
                _bloodDonorProfileState.update {
                    it.copy(
                        isBloodGroupFilterOpen = !it.isBloodGroupFilterOpen
                    )
                }
            }

            is BloodDonorEvent.SetBloodGroupFilter -> {
                _bloodDonorProfileState.update {
                    it.copy(
                        selectedBloodGroup = event.bloodGroup
                    )
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
                _bloodDonorProfileState.update {
                    it.copy(
                        selectedState = event.state
                    )
                }
                getListOfDonorsWithoutCurrentUser()
            }
        }
    }


    fun clearAllState() {
        // Resetting the blood donor profile to its default state
        _bloodDonorModel.value = BloodDonorModel()  // or initialize with defaults if necessary

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