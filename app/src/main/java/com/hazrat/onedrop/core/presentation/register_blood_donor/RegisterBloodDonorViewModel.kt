package com.hazrat.onedrop.core.presentation.register_blood_donor

import android.content.Context
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.hazrat.onedrop.core.domain.model.BloodDonorModel
import com.hazrat.onedrop.core.domain.model.cities_model.CitiesModel
import com.hazrat.onedrop.core.domain.model.cities_model.CitiesModelItem
import com.hazrat.onedrop.core.domain.model.district_model.DistrictListModel
import com.hazrat.onedrop.core.domain.model.district_model.DistrictListModelItem
import com.hazrat.onedrop.core.domain.repository.BloodDonorRepository
import com.hazrat.onedrop.core.domain.usecase.AllUseCases
import com.hazrat.onedrop.core.presentation.blood_donor_screen.asSuccessUiText
import com.hazrat.onedrop.core.presentation.blood_donor_screen.asUiText
import com.hazrat.onedrop.util.datastore.AppDataStore
import com.hazrat.onedrop.util.datastore.DataStorePreference
import com.hazrat.onedrop.util.event.ChannelEvent
import com.hazrat.onedrop.util.event.ChannelEvent.*
import com.hazrat.onedrop.util.results.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 * Created on 20-12-2024
 */

@HiltViewModel
class RegisterBloodDonorViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: BloodDonorRepository,
    private val dataStorePreference: DataStorePreference,
    private val allUseCases: AllUseCases,
    private val firebaseAuth: FirebaseAuth,
    private val appDataStore: AppDataStore
) : ViewModel() {

    private val _registerBloodDonorState = MutableStateFlow(BloodDonorProfileState())
    val registerBloodDonorState: StateFlow<BloodDonorProfileState> =
        _registerBloodDonorState.asStateFlow()


    private val eventChannel = Channel<ChannelEvent>()
    val events = eventChannel.receiveAsFlow()


    private val _cityModel = MutableStateFlow(CitiesModel().getCitiesModelItem(context))

    @OptIn(FlowPreview::class)
    val filteredCityModel: StateFlow<List<CitiesModelItem>> = registerBloodDonorState
        .combine(_cityModel) { cityQuery, cityModel ->
            filterCities(cityQuery.city, cityModel)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _cityModel.value)

    private val _district = MutableStateFlow(DistrictListModel().getDistrictList(context))

    @OptIn(FlowPreview::class)
    val districtModel: StateFlow<List<DistrictListModelItem>> = registerBloodDonorState
        .combine(_district) { cityQuery, districtModel ->
            filterDistrict(query = cityQuery.district, districts = districtModel)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _district.value)


    private fun filterCities(query: String, cities: List<CitiesModelItem>): List<CitiesModelItem> {
        return if (query.isBlank()) {
            cities
        } else {
            cities.filter { it.doesMatchSearchQuery(query) }
        }
    }

    private fun filterDistrict(
        query: String,
        districts: List<DistrictListModelItem>
    ): List<DistrictListModelItem> {
        return if (query.isBlank()) {
            districts
        } else {
            districts.filter { it.doesMatchSearchQuery(query) }
        }
    }

    fun isFormValid() {
        val profile = _registerBloodDonorState.value
        val isValid = profile.name.isNotBlank() &&
                profile.contactNumber.isNotBlank() &&
                profile.district.isNotBlank() &&
                profile.contactNumber.length == 10 &&
                profile.city.isNotBlank() &&
                profile.bloodGroup != null &&
                profile.state != null &&
                profile.age.isNotBlank() &&
                profile.gender != null

        _registerBloodDonorState.update {
            it.copy(isFormValid = isValid)
        }
    }


    init {
        viewModelScope.launch {
            repository.getSelfBloodDonorProfile().collect { bloodDonorProfile ->
                _registerBloodDonorState.update {
                    it.copy(
                        name = bloodDonorProfile?.name ?: "",
                        age = bloodDonorProfile?.age ?: "",
                        contactNumber = bloodDonorProfile?.contactNumber ?: "",
                        gender = bloodDonorProfile?.gender,
                        bloodGroup = bloodDonorProfile?.bloodGroup,
                        city = bloodDonorProfile?.city?.capitalize(locale = Locale.getDefault())
                            ?: "",
                        district = bloodDonorProfile?.district ?: "",
                        state = bloodDonorProfile?.state,
                    )
                }
            }
            _registerBloodDonorState.update {
                it.copy(
                    isBloodDonorProfileExists = appDataStore.isBloodDonorRegistered.stateIn(
                        viewModelScope,
                        started = SharingStarted.Eagerly,
                        initialValue = false
                    ).value
                )
            }
        }
    }

    fun onEvent(event: RegisterBloodDonorEvent) {
        when (event) {
            RegisterBloodDonorEvent.CreateBloodDonorProfile -> {
                viewModelScope.launch {
                    val result = repository.createBloodDonorProfile(
                        bloodDonorModel = BloodDonorModel(
                            userId = firebaseAuth.uid.toString(),
                            name = _registerBloodDonorState.value.name,
                            age = _registerBloodDonorState.value.age,
                            gender = _registerBloodDonorState.value.gender,
                            bloodGroup = _registerBloodDonorState.value.bloodGroup,
                            city = _registerBloodDonorState.value.city,
                            contactNumber = _registerBloodDonorState.value.contactNumber,
                            district = _registerBloodDonorState.value.district,
                            state = _registerBloodDonorState.value.state,
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
                }
            }

            RegisterBloodDonorEvent.OnBloodDropDownClick -> {
                _registerBloodDonorState.update {
                    it.copy(isBloodDropDownOpen = !it.isBloodDropDownOpen)
                }
            }

            RegisterBloodDonorEvent.OnGenderDropDownClick -> {
                _registerBloodDonorState.update {
                    it.copy(isGenderDropDownOpen = !it.isGenderDropDownOpen)
                }
            }

            RegisterBloodDonorEvent.OnStateDropDownClick -> {
                _registerBloodDonorState.update {
                    it.copy(isStateDropDownOpen = !it.isStateDropDownOpen)
                }
            }

            is RegisterBloodDonorEvent.SetAge -> {
                _registerBloodDonorState.update {
                    it.copy(age = event.age)
                }
                isFormValid()
            }

            is RegisterBloodDonorEvent.SetAvailable -> {

            }

            is RegisterBloodDonorEvent.SetBloodGroup -> {
                _registerBloodDonorState.update {
                    it.copy(bloodGroup = event.bloodGroup)
                }
                isFormValid()
            }

            is RegisterBloodDonorEvent.SetCity -> {
                _registerBloodDonorState.update {
                    it.copy(
                        city = event.city,
                        isCityValid = allUseCases.cityValidation.invoke(
                            _registerBloodDonorState.value.city,
                            _cityModel.value
                        )
                    )
                }
                isFormValid()
            }

            is RegisterBloodDonorEvent.SetContactNumber -> {
                _registerBloodDonorState.update {
                    it.copy(contactNumber = event.contactNumber)
                }
                isFormValid()
            }

            is RegisterBloodDonorEvent.SetDistrict -> {
                _registerBloodDonorState.update {
                    it.copy(
                        district = event.district
                    )
                }
                isFormValid()
            }

            is RegisterBloodDonorEvent.SetGender -> {
                _registerBloodDonorState.update {
                    it.copy(gender = event.gender)
                }
                isFormValid()
            }

            is RegisterBloodDonorEvent.SetName -> {
                _registerBloodDonorState.update {
                    it.copy(name = event.name)
                }
                isFormValid()
            }

            is RegisterBloodDonorEvent.SetState -> {
                _registerBloodDonorState.update {
                    it.copy(state = event.state)
                }
                isFormValid()
            }

            RegisterBloodDonorEvent.OnCityDropDownClick -> {
                _registerBloodDonorState.update {
                    it.copy(isCitySearching = !it.isCitySearching)
                }
            }

            RegisterBloodDonorEvent.OnDistrictDropDownClick -> {
                _registerBloodDonorState.update {
                    it.copy(isDistrictSearching = !it.isDistrictSearching)
                }
            }
        }
    }
}