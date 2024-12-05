package com.hazrat.onedrop.auth.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.hazrat.onedrop.auth.domain.model.FirebaseUserData
import com.hazrat.onedrop.auth.domain.repository.AuthRepository
import com.hazrat.onedrop.auth.util.saveUserToFirestore
import com.hazrat.onedrop.util.RootConstants.INTERNALSTORAGEPICTUREFOLDER
import com.hazrat.onedrop.util.RootConstants.PROFILE_PICTURE
import com.hazrat.onedrop.util.results.Result
import com.hazrat.onedrop.util.results.SignInErrorResult
import com.hazrat.onedrop.util.results.SignInSuccessResult
import com.hazrat.onedrop.util.results.SignUpErrorResult
import com.hazrat.onedrop.util.results.SignUpSuccessResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.net.URL
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

/**
 * @author Hazrat Ummar Shaikh
 * Created on 02-12-2024
 */

class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : AuthRepository {


    override suspend fun signUp(
        email: String,
        password: String,
        name: String
    ): Result<SignUpSuccessResult, SignUpErrorResult> {
        return try {
            val user = createUser(email, password)
            val userId = user?.uid ?: ""
            val userData = FirebaseUserData(userId, name, email)

            saveUserToFirestore(userId, userData, firestore)
            Result.Success(SignUpSuccessResult.SIGN_UP_SUCCESS)
        } catch (e: Exception) {
            handleSignUpError(e)
        } catch (e: FirebaseAuthUserCollisionException) {
            Result.Error(SignUpErrorResult.USER_ALREADY_EXISTS)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun signIn(
        email: String,
        password: String
    ): Result<SignInSuccessResult, SignInErrorResult> {
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
            }
        }
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




    fun Context.createDirectory(): File {
        val directory = filesDir
        val file = File(directory, INTERNALSTORAGEPICTUREFOLDER)
        if (!file.exists()) file.mkdirs()
        return file
    }
}