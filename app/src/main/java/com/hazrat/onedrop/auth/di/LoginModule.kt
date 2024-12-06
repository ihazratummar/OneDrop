package com.hazrat.onedrop.auth.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.hazrat.onedrop.auth.data.repository.AuthRepositoryImpl
import com.hazrat.onedrop.auth.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object LoginModule {


    @Provides
    fun provideSignUpRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        credentialManager: androidx.credentials.CredentialManager
    ): AuthRepository {
        return AuthRepositoryImpl(
            firebaseAuth = firebaseAuth,
            firestore = firestore,
            credentialManager = credentialManager
        )
    }

}