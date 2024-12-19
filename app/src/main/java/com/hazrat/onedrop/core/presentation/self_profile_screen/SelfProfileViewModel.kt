package com.hazrat.onedrop.core.presentation.self_profile_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.onedrop.core.domain.model.BloodDonorModel
import com.hazrat.onedrop.core.domain.repository.BloodDonorRepository
import com.hazrat.onedrop.core.presentation.blood_donor_screen.asSuccessUiText
import com.hazrat.onedrop.core.presentation.blood_donor_screen.asUiText
import com.hazrat.onedrop.core.presentation.blood_donor_screen.profileErrorUiText
import com.hazrat.onedrop.core.presentation.blood_donor_screen.profileSuccessUiText
import com.hazrat.onedrop.util.event.ChannelEvent
import com.hazrat.onedrop.util.results.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 * Created on 10-12-2024
 */

@HiltViewModel
class SelfProfileViewModel @Inject constructor(
    private val bloodDonorRepository: BloodDonorRepository
) : ViewModel() {

    private val _bloodDonorModel = MutableStateFlow(BloodDonorModel())
    val bloodDonorModel: StateFlow<BloodDonorModel> = _bloodDonorModel.asStateFlow()


    private val _selfProfileState = MutableStateFlow(SelfProfileState())
    val selfProfileState: StateFlow<SelfProfileState> = _selfProfileState.asStateFlow()

    private val eventChannel = Channel<ChannelEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            bloodDonorRepository.getSelfBloodDonorProfile().collect {
                _bloodDonorModel.value = it
            }
        }
    }

    fun refresh(){
        viewModelScope.launch {
            bloodDonorRepository.getSelfBloodDonorProfile().collect {
                _bloodDonorModel.value = it
            }
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
                profile.state != null &&
                profile.age.isNotBlank() &&
                profile.gender != null

        _selfProfileState.update {
            it.copy(isFormValid = isValid)
        }
    }

    fun onEvent(event: SelfBloodDonorEvent) {
        when (event) {
            SelfBloodDonorEvent.ToggleBloodGroupDropDown -> {
                _selfProfileState.update {
                    it.copy(
                        isBloodDropDownOpen = !it.isBloodDropDownOpen
                    )
                }
            }

            SelfBloodDonorEvent.ToggleGenderDropDown -> {
                _selfProfileState.update {
                    it.copy(
                        isGenderDropDownOpen = !it.isGenderDropDownOpen
                    )
                }
            }

            SelfBloodDonorEvent.ToggleStateDropDown -> {
                _selfProfileState.update {
                    it.copy(
                        isStateDropDownOpen = !it.isStateDropDownOpen
                    )
                }
            }

            is SelfBloodDonorEvent.UpdateAgeValue -> {
                _bloodDonorModel.update {
                    it.copy(
                        age = event.age
                    )
                }
                isFormValid()
            }

            SelfBloodDonorEvent.UpdateBloodDonorProfile -> {
                viewModelScope.launch {
                   val result = bloodDonorRepository.updateBloodDonorProfile(
                       bloodDonorModel = BloodDonorModel(
                           userId = _bloodDonorModel.value.userId,
                           name = _bloodDonorModel.value.name,
                           age = _bloodDonorModel.value.age,
                           gender = _bloodDonorModel.value.gender,
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
                    when(result){
                        is Result.Error -> {
                            val errorMessage = result.error.profileErrorUiText()
                            Log.d("SelfProfileViewModel", "Error event: $errorMessage")
                            eventChannel.send(ChannelEvent.Error(errorMessage))
                        }
                        is Result.Success -> {
                            val successMessage = result.data.profileSuccessUiText()
                            Log.d("SelfProfileViewModel", "Success event: $successMessage")
                            eventChannel.send(ChannelEvent.Success(successMessage))
                        }
                    }
                }
            }

            is SelfBloodDonorEvent.UpdateBloodValue -> {
                _bloodDonorModel.update {
                    it.copy(
                        bloodGroup = event.bloodGroup
                    )
                }
                isFormValid()
            }

            is SelfBloodDonorEvent.UpdateCityValue -> {
                _bloodDonorModel.update {
                    it.copy(
                        city = event.city
                    )
                }
                isFormValid()
            }

            is SelfBloodDonorEvent.UpdateDistrictValue -> {
                _bloodDonorModel.update {
                    it.copy(
                        district = event.district
                    )
                }
                isFormValid()
            }

            is SelfBloodDonorEvent.UpdateGenderValue -> {
                _bloodDonorModel.update {
                    it.copy(
                        gender = event.gender
                    )
                }
                isFormValid()
            }

            is SelfBloodDonorEvent.UpdateNameValue -> {
                _bloodDonorModel.update {
                    it.copy(
                        name = event.name
                    )
                }
                isFormValid()
            }

            is SelfBloodDonorEvent.UpdateNumberValue -> {
                _bloodDonorModel.update {
                    it.copy(
                        contactNumber = event.number
                    )
                }
                isFormValid()
            }

            is SelfBloodDonorEvent.UpdateStateValue -> {
                _bloodDonorModel.update {
                    it.copy(
                        state = event.state
                    )
                }
                isFormValid()
            }

            SelfBloodDonorEvent.Refresh -> {
                refresh()
            }
        }
    }

}
