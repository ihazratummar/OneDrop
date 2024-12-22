package com.hazrat.onedrop.core.presentation.more_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.hazrat.onedrop.core.domain.repository.BloodDonorRepository
import com.hazrat.onedrop.util.datastore.AppDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 * Created on 21-12-2024
 */

@HiltViewModel
class MoreScreenViewModel @Inject constructor(
    private val repository: BloodDonorRepository,
    private val firebaseAuth: FirebaseAuth,
    private val appDataStore: AppDataStore
) : ViewModel() {


    private val _moreScreenState = MutableStateFlow(MoreScreenState())

    val moreScreenState: StateFlow<MoreScreenState> = combine(
        _moreScreenState,
        appDataStore.isBloodDonorRegistered.stateIn(
            viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )
    ) { state, isBloodDonorRegistered ->
        state.copy(isBloodDonorProfileExists = isBloodDonorRegistered)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MoreScreenState()
    )


    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            repository.getSelfBloodDonorProfile().collect { data ->
                _moreScreenState.value = _moreScreenState.value.copy(
                    profileName = data?.name,
                    bloodGroup = data?.bloodGroup
                )
            }
            repository.isBloodDonorProfileExists(firebaseAuth.currentUser?.uid.toString())
                .collect { state ->
                    _moreScreenState.update {
                        it.copy(isBloodDonorProfileExists = state)
                    }
                    appDataStore.setBloodDonorRegistered(state)
                }
        }
    }

    fun enableDarkTheme(enable: Boolean) {
        viewModelScope.launch {
            appDataStore.enableDarkTheme(enable)
        }
    }
}