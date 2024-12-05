package com.hazrat.onedrop.util

import kotlinx.coroutines.flow.Flow

/**
 * @author Hazrat Ummar Shaikh
 * Created on 04-12-2024
 */

interface ConnectivityObserver {

    val isConnected: Flow<Boolean>

}