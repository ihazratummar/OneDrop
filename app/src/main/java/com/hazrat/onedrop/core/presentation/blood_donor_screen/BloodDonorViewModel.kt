package com.hazrat.onedrop.core.presentation.blood_donor_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val dataStorePreference: DataStorePreference
) : ViewModel() {

    private val _donorList = MutableStateFlow<List<BloodDonorModel>>(emptyList())
    val donorList: StateFlow<List<BloodDonorModel>>  = _donorList.asStateFlow()

    val bloodDonorProfile: StateFlow<BloodDonorModel?> = repository.bloodDonorProfile

    private val _bloodDonorProfileExists = MutableStateFlow(
        BloodDonorProfileState(
            isBloodDonorProfileExists = dataStorePreference.getBloodDonorRegistered()
        )

    )
    val isBloodDonorProfileExists: StateFlow<BloodDonorProfileState>  = _bloodDonorProfileExists.asStateFlow()

    init {
        getListOfDonors()
    }

    fun refreshProfileState() {
        viewModelScope.launch {
            repository.isBloodDonorProfileExists().collect { state ->
                _bloodDonorProfileExists.update {
                    it.copy(isBloodDonorProfileExists = state)
                }
            }
        }
    }

    fun getListOfDonors(){
        viewModelScope.launch {
            repository.getListOfDonors().collect{
                _donorList.value = it
            }
        }
    }

    fun onEvent(event: BloodDonorEvent) {
        when (event) {
            is BloodDonorEvent.SetName -> {
                repository.setName(event.name)
            }

            is BloodDonorEvent.SetBloodGroup -> {
                repository.setBloodGroup(event.bloodGroup)
            }

            is BloodDonorEvent.SetAvailable -> {
                repository.setAvailable()
            }

            is BloodDonorEvent.SetContactNumber -> {
                repository.setContactNumber(event.contactNumber)
            }

            BloodDonorEvent.SetContactNumberPrivate -> TODO()

            BloodDonorEvent.SetNotificationEnabled -> {

            }

            BloodDonorEvent.SetNotificationScope -> TODO()

            is BloodDonorEvent.SetDistrict -> {
                repository.setDistrict(event.district)
            }

            is BloodDonorEvent.SetState -> {
                repository.setState(event.state)
            }

            BloodDonorEvent.CreateBloodDonorProfile -> {
                viewModelScope.launch {
                    repository.createBloodDonorProfile()
                    refreshProfileState()
                    getListOfDonors()
                }
            }

        }
    }

}