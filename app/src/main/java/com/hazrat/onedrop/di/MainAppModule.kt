package com.hazrat.onedrop.di

import android.content.Context
import com.hazrat.onedrop.util.AndroidConnectivityObserver
import com.hazrat.onedrop.util.ConnectivityObserver
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
    fun provideAndroidConnectivityObserver(@ApplicationContext context: Context): ConnectivityObserver {

        return AndroidConnectivityObserver(context = context)

    }

    @ApplicationScope
    @Provides
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(Dispatchers.IO)
    }

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope