package com.hazrat.onedrop.auth.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.hazrat.onedrop.auth.data.repository.GoogleClientImpl
import com.hazrat.onedrop.auth.domain.repository.GoogleClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }


    @Provides
    fun provideFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager {
        return CredentialManager.create(context)
    }

    @Provides
    fun provideGoogleAuthClient(
        firebaseAuth: FirebaseAuth,
        credentialManager: CredentialManager,
        firestore: FirebaseFirestore
    ): GoogleClient {
        return GoogleClientImpl(
            firebaseAuth = firebaseAuth, credentialManager = credentialManager,
            firestore = firestore
        )

    }


}