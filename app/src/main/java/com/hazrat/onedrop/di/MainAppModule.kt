package com.hazrat.onedrop.di

import android.content.Context
import com.hazrat.onedrop.util.AndroidConnectivityObserver
import com.hazrat.onedrop.util.ConnectivityObserver
import com.hazrat.onedrop.util.datastore.DataStorePreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier


@Module
@InstallIn(SingletonComponent::class)
object MainAppModule {

    @Provides
    fun provideAndroidConnectivityObserver(
        @ApplicationContext context: Context,
        dataStorePreference: DataStorePreference
    ): ConnectivityObserver {

        return AndroidConnectivityObserver(
            context = context,
            dataStorePreference = dataStorePreference
        )

    }

    @ApplicationScope
    @Provides
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(Dispatchers.IO)
    }

    @Provides
    fun provideDataStorePreference(@ApplicationContext context: Context): DataStorePreference {
        return DataStorePreference(context)
    }

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope

