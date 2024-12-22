package com.hazrat.onedrop.core.presentation.more_screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.onedrop.util.datastore.AppDataStore
import com.hazrat.onedrop.util.datastore.DataStorePreference
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
class SettingViewModel @Inject constructor(
    private val appDataStore: AppDataStore,
) : ViewModel() {

    private val _settingState = MutableStateFlow(SettingScreenState())
    val settingState: StateFlow<SettingScreenState> = combine(
        _settingState,
        appDataStore.isBloodDonorRegistered.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = false
        ),
        appDataStore.isDarkThemeEnabled.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = false
        )
    ) { state, isDonorProfileExists , isDarkThemeEnabled ->
        state.copy(isDonorProfileExists = isDonorProfileExists, selectedTheme = isDarkThemeEnabled)
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = SettingScreenState()
    )


    fun onEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.ToggleThemeDropDown -> {
                _settingState.update {
                    it.copy(selectedTheme = !it.selectedTheme)
                }
                viewModelScope.launch {
                    appDataStore.enableDarkTheme(_settingState.value.selectedTheme)
                }
            }
        }
    }
}