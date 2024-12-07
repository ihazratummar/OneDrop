package com.hazrat.onedrop.core.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hazrat.onedrop.core.data.repository.BloodDonorRepositoryImpl
import com.hazrat.onedrop.core.domain.repository.BloodDonorRepository
import com.hazrat.onedrop.util.datastore.DataStorePreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    @Singleton
    @Provides
    fun provideBloodDonorRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        dataStorePreference: DataStorePreference
    ): BloodDonorRepository = BloodDonorRepositoryImpl(
        firebaseAuth = firebaseAuth,
        firestore = firestore,
        dataStorePreference = dataStorePreference
    )

}