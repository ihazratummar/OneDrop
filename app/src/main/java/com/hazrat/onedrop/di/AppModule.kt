package com.hazrat.onedrop.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.hazrat.onedrop.util.datastore.DatastoreConstant.APP_DATA_STORE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    @Named(APP_DATA_STORE)
    fun provideAppDataStore(
        @ApplicationContext context: Context
    ) : DataStore<Preferences> = PreferenceDataStoreFactory.create{
        context.preferencesDataStoreFile(APP_DATA_STORE)
    }

}