package com.hazrat.onedrop.core.presentation.blood_donor_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.hazrat.onedrop.core.domain.model.BloodDonorModel
import com.hazrat.onedrop.core.domain.repository.BloodDonorRepository
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
 * Created on 06-12-2024
 */

@HiltViewModel
class BloodDonorViewModel @Inject constructor(
    private val repository: BloodDonorRepository,
    private val dataStorePreference: DataStorePreference,
    firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _donorList = MutableStateFlow<List<BloodDonorModel>>(emptyList())
    val donorList: StateFlow<List<BloodDonorModel>> = _donorList.asStateFlow()

    private val _bloodDonorProfile = MutableStateFlow(BloodDonorModel())
    val bloodDonorProfile: StateFlow<BloodDonorModel> = _bloodDonorProfile.asStateFlow()

    private val _bloodDonorProfileState = MutableStateFlow(
        BloodDonorProfileState(
            isBloodDonorProfileExists = dataStorePreference.getBloodDonorRegistered()
        )

    )

    val userId = firebaseAuth.currentUser?.uid

    val bloodDonorProfileState: StateFlow<BloodDonorProfileState> =
        _bloodDonorProfileState.asStateFlow()

    init {
        getListOfDonors()
    }

    fun refreshProfileState() {
        viewModelScope.launch {
            repository.isBloodDonorProfileExists().collect { state ->
                _bloodDonorProfileState.update {
                    it.copy(isBloodDonorProfileExists = state)
                }
            }
        }
    }

    fun getListOfDonors() {
        viewModelScope.launch {
            repository.getListOfDonors().collect {
                _donorList.value = it
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
                    repository.createBloodDonorProfile(
                        bloodDonorModel = BloodDonorModel(
                            userId = this@BloodDonorViewModel.userId.toString(),
                            name = _bloodDonorProfile.value.name,
                            bloodGroup = _bloodDonorProfile.value.bloodGroup,
                            available = _bloodDonorProfile.value.available,
                            contactNumber = _bloodDonorProfile.value.contactNumber,
                            district = _bloodDonorProfile.value.district,
                            isContactNumberPrivate = _bloodDonorProfile.value.isContactNumberPrivate,
                            notificationEnabled = _bloodDonorProfile.value.notificationEnabled,
                            notificationScope = _bloodDonorProfile.value.notificationScope,
                        )
                    )
                    refreshProfileState()
                    getListOfDonors()
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
        }
    }

}