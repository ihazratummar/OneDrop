package com.hazrat.onedrop.auth.util

import com.google.firebase.firestore.FirebaseFirestore
import com.hazrat.onedrop.auth.presentation.UserData
import com.hazrat.onedrop.auth.util.Constant.FIRESTORE_COLLECTION
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

/**
 * @author Hazrat Ummar Shaikh
 * Created on 02-12-2024
 */

suspend fun saveUserToFirestore(userId: String, userData: UserData, firestore: FirebaseFirestore) {
    suspendCancellableCoroutine<Unit> { continuation ->
        firestore.collection(FIRESTORE_COLLECTION).document(userId)
            .set(userData)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    continuation.resume(Unit, null)
                } else {
                    continuation.resumeWithException(it.exception ?: Exception("Unknown Error"))
                }
            }
    }
}