package com.hazrat.onedrop.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.onedrop.util.ConnectivityObserver
import com.hazrat.onedrop.util.datastore.DataStorePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 * Created on 04-12-2024
 */

@HiltViewModel
class MainViewModel @Inject constructor(
    private val connectivityObserver: ConnectivityObserver,
    private val dataStorePreference: DataStorePreference
) : ViewModel() {


    private val _isConnected = MutableStateFlow(dataStorePreference.getNetworkBoolean())

    val isConnected : StateFlow<Boolean> = _isConnected.asStateFlow()

    init {
        viewModelScope.launch {
            connectivityObserver.isConnected.collect { status ->
                _isConnected.value = status
            }
        }
    }

}