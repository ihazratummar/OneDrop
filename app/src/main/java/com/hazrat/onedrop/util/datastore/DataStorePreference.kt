package com.hazrat.onedrop.util.datastore

import android.content.Context
import com.hazrat.onedrop.util.datastore.DatastoreConstant.DONOR_REGISTERED_KEY
import com.hazrat.onedrop.util.datastore.DatastoreConstant.PREF_NAME
import com.hazrat.onedrop.util.datastore.DatastoreConstant.NETWORK_STATUS_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 * Created on 05-12-2024
 */

class DataStorePreference @Inject constructor(
    @ApplicationContext private val context: Context
) {


    fun setNetworkBoolean(isSplashScreen: Boolean) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit().putBoolean(NETWORK_STATUS_KEY, isSplashScreen).apply()
    }

    fun getNetworkBoolean(): Boolean {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pref.getBoolean(NETWORK_STATUS_KEY, false)
    }

    fun setBloodDonorRegistered(isRegistered: Boolean) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit().putBoolean(DONOR_REGISTERED_KEY, false).apply()
    }

    fun getBloodDonorRegistered(): Boolean {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pref.getBoolean(DONOR_REGISTERED_KEY, false)
    }

}