package com.hazrat.onedrop.auth.data.repository

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.hazrat.onedrop.BuildConfig
import com.hazrat.onedrop.auth.domain.repository.AuthRepository
import com.hazrat.onedrop.auth.presentation.AuthState
import com.hazrat.onedrop.auth.presentation.ProfileState
import com.hazrat.onedrop.auth.presentation.UserData
import com.hazrat.onedrop.auth.util.saveUserToFirestore
import com.hazrat.onedrop.util.RootConstants.INTERNALSTORAGEPICTUREFOLDER
import com.hazrat.onedrop.util.datastore.DataStorePreference
import com.hazrat.onedrop.util.results.Result
import com.hazrat.onedrop.util.results.SignInErrorResult
import com.hazrat.onedrop.util.results.SignInSuccessResult
import com.hazrat.onedrop.util.results.SignUpErrorResult
import com.hazrat.onedrop.util.results.SignUpSuccessResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

/**
 * @author Hazrat Ummar Shaikh
 * Created on 02-12-2024
 */

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val credentialManager: CredentialManager,
    private val dataStorePreference: DataStorePreference

) : AuthRepository {
    private lateinit var activity: Context


    private val _authState = MutableLiveData<AuthState>()
    override val authState: LiveData<AuthState> = _authState

    private val _profileState = MutableStateFlow(ProfileState())
    override val profileState = _profileState.asStateFlow()

    init {
        firebaseAuth.addAuthStateListener { auth ->
            val user = auth.currentUser
            if (user != null) {
                _authState.value = AuthState.Authenticated
                fetchUserData()  // Ensure user data is fetched
            } else {
                _authState.value = AuthState.Unauthenticated
            }
        }
    }


    override suspend fun signUp(
        email: String,
        password: String,
        name: String
    ): Result<SignUpSuccessResult, SignUpErrorResult> {
        _authState.value = AuthState.Loading
        return try {
            val user = createUser(email, password)
            val userId = user?.uid ?: ""
            val userData = UserData(userId, name, email)

            saveUserToFirestore(userId, userData, firestore)
            _profileState.update {
                it.copy(
                    userData = userData
                )
            }
            _authState.value = AuthState.Authenticated
            Result.Success(SignUpSuccessResult.SIGN_UP_SUCCESS)

        } catch (e: Exception) {
            _authState.value = AuthState.Unauthenticated
            handleSignUpError(e)
        } catch (e: FirebaseAuthUserCollisionException) {
            _authState.value = AuthState.Unauthenticated
            Result.Error(SignUpErrorResult.USER_ALREADY_EXISTS)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun signIn(
        email: String,
        password: String
    ): Result<SignInSuccessResult, SignInErrorResult> {
        _authState.value = AuthState.Loading
        return suspendCancellableCoroutine { continuation ->
            try {
                // Initiating the Firebase sign-in process
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(
                                Result.Success(SignInSuccessResult.SIGN_IN_SUCCESS),
                                null
                            )
                            val userId = firebaseAuth.currentUser?.uid ?: ""
                            firestore.collection("users").document(userId)
                                .get()
                                .addOnSuccessListener {
                                    val userData = it.toObject(UserData::class.java)
                                    _profileState.update {
                                        it.copy(
                                            userData = userData
                                        )
                                    }
                                }
                            _authState.value = AuthState.Authenticated
                        } else {
                            val exception = task.exception
                            when (exception) {
                                is FirebaseAuthInvalidCredentialsException -> {
                                    // Check if it's an invalid email or password issue
                                    val errorCode = exception.errorCode
                                    if (errorCode == "ERROR_INVALID_EMAIL") {
                                        continuation.resume(
                                            Result.Error(SignInErrorResult.INVALID_EMAIL),
                                            null
                                        )
                                    } else if (errorCode == "ERROR_WRONG_PASSWORD") {
                                        continuation.resume(
                                            Result.Error(SignInErrorResult.INVALID_PASSWORD),
                                            null
                                        )
                                    } else {
                                        continuation.resume(
                                            Result.Error(SignInErrorResult.UNKNOWN_ERROR),
                                            null
                                        )
                                    }
                                }

                                is FirebaseAuthInvalidUserException -> {
                                    continuation.resume(
                                        Result.Error(SignInErrorResult.INVALID_EMAIL),
                                        null
                                    )
                                }

                                else -> {
                                    continuation.resume(
                                        Result.Error(SignInErrorResult.UNKNOWN_ERROR),
                                        null
                                    )
                                }
                            }
                            _authState.value = AuthState.Unauthenticated
                        }
                    }
            } catch (e: Exception) {
                // Handling exceptions outside the Firebase task
                when (e) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        val errorCode = e.errorCode
                        if (errorCode == "ERROR_INVALID_EMAIL") {
                            continuation.resume(
                                Result.Error(SignInErrorResult.INVALID_EMAIL),
                                null
                            )
                        } else {
                            continuation.resume(
                                Result.Error(SignInErrorResult.UNKNOWN_ERROR),
                                null
                            )
                        }
                    }

                    else -> {
                        continuation.resume(Result.Error(SignInErrorResult.UNKNOWN_ERROR), null)
                    }
                }
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    override suspend fun googleCredentialSignIn(): Boolean {
        if (googleCredentialIsSignIn()) {
            return true
        }

        try {
            val result = buildCredentialResponse()
            _authState.value = AuthState.Authenticated
            return handleSignInResult(result)

        } catch (e: Exception) {
            _authState.value = AuthState.Unauthenticated
            e.printStackTrace()
            if (e is CancellationException) throw e
            return false
        }
    }

    override fun googleCredentialIsSignIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun setActivity(activityContext: Context) {
        this.activity = activityContext
    }

    override suspend fun signOut() {

        dataStorePreference.clearAllData()

        credentialManager.clearCredentialState(
            ClearCredentialStateRequest()
        )
        firebaseAuth.signOut()
        _authState.value = AuthState.Unauthenticated
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun createUser(email: String, password: String): FirebaseUser? {
        return suspendCancellableCoroutine { continuation ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        continuation.resume(it.result?.user, null)
                    } else {
                        continuation.resumeWithException(it.exception ?: Exception("Unknown Error"))
                    }
                }
        }
    }

    private fun handleSignUpError(e: Exception): Result.Error<SignUpSuccessResult, SignUpErrorResult> {
        return when (e) {
            is FirebaseAuthInvalidCredentialsException -> Result.Error(SignUpErrorResult.INVALID_EMAIL)
            is FirebaseAuthWeakPasswordException -> Result.Error(SignUpErrorResult.INVALID_PASSWORD)
            is FirebaseAuthUserCollisionException -> Result.Error(SignUpErrorResult.USER_ALREADY_EXISTS)
            else -> Result.Error(SignUpErrorResult.UNKNOWN_ERROR)
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

                val userData = UserData(
                    userId = authResult.user?.uid ?: "",
                    fullName = authResult.user?.displayName ?: "",
                    email = authResult.user?.email ?: ""
                )
                val userId = authResult?.user?.uid ?: ""
                saveUserToFirestore(userId, userData, firestore)
                _profileState.update {
                    it.copy(
                        userData = userData
                    )
                }
                return authResult.user != null
            } catch (e: GoogleIdTokenParsingException) {
                e.printStackTrace()
                return false
            }

        } else {
            return false
        }
    }


    fun Context.createDirectory(): File {
        val directory = filesDir
        val file = File(directory, INTERNALSTORAGEPICTUREFOLDER)
        if (!file.exists()) file.mkdirs()
        return file
    }


    override fun checkAuthStatus() {
        if (firebaseAuth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
            fetchUserData()
        }
    }


    override fun fetchUserData() {
        _authState.value = AuthState.Loading
        val userId = firebaseAuth.currentUser?.uid ?: return
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val userData = document.toObject(UserData::class.java)
                    _profileState.update {
                        it.copy(
                            userData = userData
                        )
                    }
                } else {
                    _profileState.update {
                        it.copy(
                            userData = UserData(fullName = "", email = "")
                        )
                    }
                }
                _authState.value = AuthState.Authenticated
            }.addOnFailureListener { e ->
                _authState.value = AuthState.Error(e.message ?: "Something went wrong")
            }
    }
}