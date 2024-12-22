package com.hazrat.onedrop.core.presentation.self_profile_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hazrat.onedrop.core.domain.model.BloodDonorModel
import com.hazrat.onedrop.core.domain.repository.BloodDonorRepository
import com.hazrat.onedrop.util.RootConstants.BLOOD_DONORS
import com.hazrat.onedrop.util.datastore.AppDataStore
import com.hazrat.onedrop.util.datastore.DataStorePreference
import com.hazrat.onedrop.util.event.ChannelEvent
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
    private val bloodDonorRepository: BloodDonorRepository,
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val dataStorePreference: DataStorePreference,
    private val appDataStore: AppDataStore
) : ViewModel() {

    private val _bloodDonorModel = MutableStateFlow(BloodDonorModel())
    val bloodDonorModel: StateFlow<BloodDonorModel> = _bloodDonorModel.asStateFlow()


    private val eventChannel = Channel<ChannelEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            bloodDonorRepository.getSelfBloodDonorProfile().collect {
                if (it != null) {
                    _bloodDonorModel.value = it
                }
            }
        }
    }

    fun onEvent(event: SelfBloodDonorEvent) {
        when (event) {
            SelfBloodDonorEvent.Refresh -> {
                refresh()
            }

            SelfBloodDonorEvent.ToggleAvailable -> {
                _bloodDonorModel.update {
                    it.copy(
                        available = !it.available
                    )
                }
                viewModelScope.launch {
                    firebaseFirestore.collection(BLOOD_DONORS)
                        .document(firebaseAuth.currentUser?.uid.toString()).update(
                        "available", _bloodDonorModel.value.available
                    )
                    appDataStore.setBloodDonorAvailable(_bloodDonorModel.value.available)
                }
            }
        }
    }
}
