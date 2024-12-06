package com.hazrat.onedrop.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import com.hazrat.onedrop.util.datastore.DataStorePreference
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 * Created on 04-12-2024
 */

class AndroidConnectivityObserver @Inject constructor(
    @ApplicationContext context: Context,
    private val dataStorePreference: DataStorePreference
) : ConnectivityObserver {

    val connectivityManager = context.getSystemService<ConnectivityManager>()!!

    override val isConnected: Flow<Boolean>
        @RequiresApi(Build.VERSION_CODES.N)
        get() = callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    trySend(true)
                    dataStorePreference.setNetworkBoolean(true)
                }


                override fun onLost(network: Network) {
                    super.onLost(network)
                    trySend(false)
                    dataStorePreference.setNetworkBoolean(false)
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    trySend(false)
                    dataStorePreference.setNetworkBoolean(false)
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    val connected = networkCapabilities.hasCapability(
                        NetworkCapabilities.NET_CAPABILITY_VALIDATED
                    )
                    trySend(connected)
                    dataStorePreference.setNetworkBoolean(connected)
                }
            }

            connectivityManager.registerDefaultNetworkCallback(callback)

            awaitClose {
                connectivityManager.registerDefaultNetworkCallback(callback)
            }
        }
}