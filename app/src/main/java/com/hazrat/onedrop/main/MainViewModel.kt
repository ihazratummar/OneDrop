package com.hazrat.onedrop.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.onedrop.util.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 * Created on 04-12-2024
 */

@HiltViewModel
class MainViewModel @Inject constructor(
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {


    val isConnected = connectivityObserver.isConnected.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        false
    )

}