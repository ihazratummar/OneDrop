package com.hazrat.onedrop.util.datastore

import android.content.res.Configuration
import android.content.res.Resources
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.hazrat.onedrop.util.datastore.DatastoreConstant.APP_DATA_STORE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

/**
 * @author Hazrat Ummar Shaikh
 * Created on 21-12-2024
 */

class AppDataStore @Inject constructor(
    @Named(APP_DATA_STORE) private val oneDropAppDataStore: DataStore<Preferences>
) {

    private object DataStoreKeys {
        const val THEME_KEY = "THEME_KEY"
        const val DONOR_AVAILABLE_KEY = "DONOR_AVAILABLE_KEY"
        const val DONOR_REGISTERED_KEY = "donor_register_key"

        val donorAvailableKey = booleanPreferencesKey(DONOR_AVAILABLE_KEY)
        val themeKey = booleanPreferencesKey(THEME_KEY)
        val donorRegisteredKey = booleanPreferencesKey(DONOR_REGISTERED_KEY)
    }

    private val systemTheme =
        when(Resources.getSystem().configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK){
            Configuration.UI_MODE_NIGHT_YES -> { true }
            Configuration.UI_MODE_NIGHT_NO -> { false }
            else -> {false}
        }

    suspend fun enableDarkTheme(enable: Boolean) {
        oneDropAppDataStore.edit { it[DataStoreKeys.themeKey] = enable }
    }

    val isDarkThemeEnabled : Flow<Boolean> = oneDropAppDataStore.data.map {
        it[DataStoreKeys.themeKey] ?: systemTheme
    }

    // Blood Donor availability functions
    suspend fun setBloodDonorAvailable(isAvailable: Boolean) {
        oneDropAppDataStore.edit { it[DataStoreKeys.donorAvailableKey] = isAvailable }
    }

    val isBloodDonorAvailable: Flow<Boolean> = oneDropAppDataStore.data.map {
        it[DataStoreKeys.donorRegisteredKey] == true
    }

    suspend fun setBloodDonorRegistered(isRegistered: Boolean) {
        oneDropAppDataStore.edit { it[DataStoreKeys.donorRegisteredKey] = isRegistered }
    }

    val isBloodDonorRegistered: Flow<Boolean> = oneDropAppDataStore.data.map {
        it[DataStoreKeys.donorRegisteredKey] == true
    }

}