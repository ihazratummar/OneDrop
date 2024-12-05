package com.hazrat.onedrop.auth.data.repository

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.hazrat.onedrop.BuildConfig
import com.hazrat.onedrop.auth.domain.model.FirebaseUserData
import com.hazrat.onedrop.auth.domain.repository.GoogleClient
import com.hazrat.onedrop.auth.util.Constant.FIRESTORE_COLLECTION
import com.hazrat.onedrop.auth.util.saveUserToFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resumeWithException

/**
 * @author Hazrat Ummar Shaikh
 * Created on 01-12-2024
 */

class GoogleClientImpl @Inject constructor(
    private val credentialManager: CredentialManager,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : GoogleClient {

    private lateinit var activity: Context

    override fun setActivity(activityContext: Context) {
        this.activity = activityContext
    }

    override fun isSignIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override suspend fun signIn(): Boolean {
        if (isSignIn()) {
            return true
        }

        try {
            val result = buildCredentialResponse()
            return handleSignInResult(result)

        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            return false
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun handleSignInResult(result: GetCredentialResponse): Boolean {
        val credential = result.credential
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {


            try {
                val tokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                val authCredential = GoogleAuthProvider.getCredential(tokenCredential.idToken, null)
                val authResult = firebaseAuth.signInWithCredential(authCredential).await()

                val userData = FirebaseUserData(
                    userId = authResult.user?.uid ?: "",
                    fullName = authResult.user?.displayName ?: "",
                    email = authResult.user?.email ?: "",
                    profilePictureUrl = authResult?.user?.photoUrl.toString()
                )
                val userId = authResult?.user?.uid ?: ""
                saveUserToFirestore(userId, userData, firestore)
                return authResult.user != null
            } catch (e: GoogleIdTokenParsingException) {
                e.printStackTrace()
                return false
            }

        } else {
            return false
        }
    }


    private suspend fun buildCredentialResponse(): GetCredentialResponse {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(
                GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(BuildConfig.GOOGLE_SIGN_WEB_SDK_CLIENT)
                    .setAutoSelectEnabled(false)
                    .build()
            ).build()

        return credentialManager.getCredential(context = activity, request = request)
    }


    override suspend fun signOut() {
        credentialManager.clearCredentialState(
            ClearCredentialStateRequest()
        )
        firebaseAuth.signOut()
    }
}