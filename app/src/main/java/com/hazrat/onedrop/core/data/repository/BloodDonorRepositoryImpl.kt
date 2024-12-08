package com.hazrat.onedrop.core.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.hazrat.onedrop.core.domain.model.BloodDonorModel
import com.hazrat.onedrop.core.domain.repository.BloodDonorRepository
import com.hazrat.onedrop.util.RootConstants.BLOOD_DONORS
import com.hazrat.onedrop.util.results.BloodDonorProfileError
import com.hazrat.onedrop.util.results.BloodDonorProfileSuccess
import com.hazrat.onedrop.util.results.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 * Created on 06-12-2024
 */

class BloodDonorRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : BloodDonorRepository {


    override suspend fun createBloodDonorProfile(bloodDonorModel: BloodDonorModel): Result<BloodDonorProfileSuccess, BloodDonorProfileError> {
        return suspendCancellableCoroutine { continuation ->
            try {
                firestore.collection(BLOOD_DONORS).document(bloodDonorModel.userId)
                    .set(bloodDonorModel)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(
                                Result.Success(BloodDonorProfileSuccess.SUCCESS),
                                null
                            )
                        } else {
                            continuation.resume(
                                Result.Error(BloodDonorProfileError.FAILED),
                                null
                            )
                        }
                    }
            } catch (_: Exception) {
                continuation.resume(
                    Result.Error(BloodDonorProfileError.FAILED),
                    null
                )
            }
        }
    }

    //TODo: Solve Error Decrypting Data
    override suspend fun getListOfAllDonors(): Flow<List<BloodDonorModel>> = flow {
        try {
            val donorsList = mutableListOf<BloodDonorModel>()
            val querySnapshot = firestore.collection(BLOOD_DONORS).get().await()

            for (document in querySnapshot.documents) {
                val donor = document.toObject(BloodDonorModel::class.java)
                donor?.let {donorsList.add(it)}
            }
            Log.d("BloodDonorRepositoryImpl", "getting the list: ${donorsList.size}")
            emit(donorsList)
        } catch (e: Exception) {
            Log.d("BloodDonorRepositoryImpl", "Error getting the list: ${e.message}")
            emit(emptyList())
        }
    }.catch { e ->
        Log.d("BloodDonorRepositoryImpl", "Error getting the list: ${e.message}")
        emit(emptyList())
    }

    override suspend fun getListOfDonorsWithoutCurrentUser(userId: String): Flow<List<BloodDonorModel>> = flow {
            try {
                val donorsList = mutableListOf<BloodDonorModel>()
                val querySnapshot = firestore.collection(BLOOD_DONORS).get().await()

                for (document in querySnapshot.documents) {
                    val donor = document.toObject(BloodDonorModel::class.java)
                    if (userId != donor?.userId) {
                        donor?.let {donorsList.add(it)}
                    }
                }
                Log.d("BloodDonorRepositoryImpl", "getting the list: ${donorsList.size}")
                emit(donorsList)
            } catch (e: Exception) {
                Log.d("BloodDonorRepositoryImpl", "Error getting the list: ${e.message}")
                emit(emptyList())
            }
        }

    override suspend fun isBloodDonorProfileExists(userId: String): Flow<Boolean> = flow {
        if (userId.isEmpty()) {
            Log.d("BloodDonorRepositoryImpl", "UserId is not valid")
            emit(false)
            return@flow
        }

        try {
            val donorProfile =
                firestore.collection(BLOOD_DONORS).document(userId)
                    .get().await()
            val donor = donorProfile.toObject(BloodDonorModel::class.java)

            if (donor?.userId == userId) {
                emit(true)
            } else {
                emit(false)
            }
        } catch (e: Exception) {
            Log.d(
                "BloodDonorRepositoryImpl",
                "Error checking if donor profile exists: ${e.message}"
            )
            emit(false)
        }
    }
}