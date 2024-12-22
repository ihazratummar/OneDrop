package com.hazrat.onedrop.core.presentation.more_screen.settings

/**
 * @author Hazrat Ummar Shaikh
 * Created on 21-12-2024
 */

sealed interface SettingsEvent {

    data object ToggleThemeDropDown : SettingsEvent

}
