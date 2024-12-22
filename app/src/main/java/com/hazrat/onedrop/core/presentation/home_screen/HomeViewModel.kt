package com.hazrat.onedrop.core.presentation.home_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.onedrop.core.domain.model.BloodDonorModel
import com.hazrat.onedrop.core.domain.repository.BloodDonorRepository
import com.hazrat.onedrop.util.datastore.AppDataStore
import com.hazrat.onedrop.util.datastore.DataStorePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 * Created on 22-12-2024
 */


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataStorePreference: DataStorePreference,
    private val appDataStore: AppDataStore,
    private val repository: BloodDonorRepository,
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = combine(
        _homeState,
        appDataStore.isBloodDonorRegistered.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        ),
        appDataStore.isBloodDonorAvailable.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        ),
    ) { state, isRegistered, isAvailable ->
        state.copy(
            isDonorProfileExists = isRegistered,
            isDonorAvailable = isAvailable
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        HomeState()
    )



    private val _allBloodDonorList = MutableStateFlow<List<BloodDonorModel>>(emptyList())
    val allBloodDonorList: StateFlow<List<BloodDonorModel>> = _allBloodDonorList.asStateFlow()

    init {
        viewModelScope.launch {
            homeState.collect { updatedState ->
                Log.d(
                    "HomeViewModel",
                    "State Updated: isRegistered=${updatedState.isDonorProfileExists}, isAvailable=${updatedState.isDonorAvailable}"
                )
            }
        }
        getListOfAllDonors()
    }

    fun getListOfAllDonors() {
        viewModelScope.launch {
            repository.getListOfAllDonors().collect { donors ->
                _allBloodDonorList.value = donors
            }
        }
    }
}
