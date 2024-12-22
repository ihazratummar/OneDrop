package com.hazrat.onedrop.util.event

import com.hazrat.onedrop.util.UiText

/**
 * @author Hazrat Ummar Shaikh
 * Created on 10-12-2024
 */

sealed interface ChannelEvent {
    data class Success(val success: UiText) : ChannelEvent
    data class Error(val error: UiText) : ChannelEvent
}