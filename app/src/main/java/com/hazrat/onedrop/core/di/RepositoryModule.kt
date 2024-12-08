package com.hazrat.onedrop.core.di

import com.google.firebase.firestore.FirebaseFirestore
import com.hazrat.onedrop.core.data.repository.BloodDonorRepositoryImpl
import com.hazrat.onedrop.core.domain.repository.BloodDonorRepository
import com.hazrat.onedrop.util.encrytion.EncryptionUtil
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
        firestore: FirebaseFirestore,
    ): BloodDonorRepository = BloodDonorRepositoryImpl(
        firestore = firestore
    )

}