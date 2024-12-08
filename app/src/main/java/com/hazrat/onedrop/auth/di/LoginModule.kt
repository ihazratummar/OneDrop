package com.hazrat.onedrop.auth.di

import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hazrat.onedrop.auth.data.repository.AuthRepositoryImpl
import com.hazrat.onedrop.auth.domain.repository.AuthRepository
import com.hazrat.onedrop.util.datastore.DataStorePreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object LoginModule {


    @Provides
    fun provideSignUpRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        credentialManager: CredentialManager,
        dataStorePreference: DataStorePreference
    ): AuthRepository {
        return AuthRepositoryImpl(
            firebaseAuth = firebaseAuth,
            firestore = firestore,
            credentialManager = credentialManager,
            dataStorePreference = dataStorePreference
        )
    }

}