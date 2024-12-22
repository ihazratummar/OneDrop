package com.hazrat.onedrop.core.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hazrat.onedrop.core.domain.model.BloodDonorModel
import com.hazrat.onedrop.core.domain.model.BloodGroup
import com.hazrat.onedrop.core.domain.model.State
import com.hazrat.onedrop.core.domain.repository.BloodDonorRepository
import com.hazrat.onedrop.util.RootConstants.BLOOD_DONORS
import com.hazrat.onedrop.util.results.BloodDonorProfileError
import com.hazrat.onedrop.util.results.BloodDonorProfileSuccess
import com.hazrat.onedrop.util.results.Result
import com.hazrat.onedrop.util.results.SelfProfileError
import com.hazrat.onedrop.util.results.SelfProfileSuccess
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
    private val firebaseAuth: FirebaseAuth,
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

    override suspend fun updateBloodDonorProfile(bloodDonorModel: BloodDonorModel): Result<SelfProfileSuccess, SelfProfileError> {
        return suspendCancellableCoroutine { continuation ->
            try {
                val data: MutableMap<String, Any?> = hashMapOf(
                    "name" to bloodDonorModel.name,
                    "age" to bloodDonorModel.age,
                    "bloodGroup" to bloodDonorModel.bloodGroup?.name,
                    "city" to bloodDonorModel.city,
                    "contactNumber" to bloodDonorModel.contactNumber,
                    "district" to bloodDonorModel.district,
                    "state" to bloodDonorModel.state?.name,
                    "gender" to bloodDonorModel.gender?.name,
                    // Add other fields as necessary
                )

                firestore.collection(BLOOD_DONORS).document(bloodDonorModel.userId)
                    .update(data)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            continuation.resume(
                                Result.Success(SelfProfileSuccess.UPDATED),
                                null
                            )
                        }else{
                            continuation.resume(
                                Result.Error(SelfProfileError.UPDATE_FAILED),
                                null
                            )
                        }
                    }
            } catch (_: Exception) {
                continuation.resume(
                    Result.Error(SelfProfileError.UPDATE_FAILED),
                    null
                )
            }
        }
    }


    override suspend fun getListOfAllDonors(): Flow<List<BloodDonorModel>> = flow {
        try {
            val donorsList = mutableListOf<BloodDonorModel>()
            val querySnapshot = firestore.collection(BLOOD_DONORS).get().await()

            for (document in querySnapshot.documents) {
                val donor = document.toObject(BloodDonorModel::class.java)
                donor?.let { donorsList.add(it) }
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

    override suspend fun getListOfDonorsWithoutCurrentUser(
        userId: String,
        bloodGroup: BloodGroup?,
        state: State?
    ): Flow<List<BloodDonorModel>> = flow {
        try {
            val donorsList = mutableListOf<BloodDonorModel>()
            val querySnapshot = firestore.collection(BLOOD_DONORS).get().await()

            for (document in querySnapshot.documents) {
                val donor = document.toObject(BloodDonorModel::class.java)
                if (userId != donor?.userId &&
                    (bloodGroup == null || donor?.bloodGroup == bloodGroup) &&
                    (state == null || donor?.state == state)
                ) {
                    donor?.let { donorsList.add(it) }
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

    override suspend fun getBloodDonorProfile(userId: String): Flow<BloodDonorModel> = flow {
        try {
            // Fetch the document from Firestore
            val document = firestore.collection(BLOOD_DONORS).document(userId).get().await()
            val donor = document.toObject(BloodDonorModel::class.java)

            // Emit the donor profile if it matches the userId
            if (userId == donor?.userId) {
                emit(donor)
            } else {
                throw Exception("No donor found with the provided userId.")
            }
        } catch (e: Exception) {
            // Log the error and re-emit it as an exception in the flow
            Log.d("BloodDonorRepositoryImpl", "Error getting the donor profile: ${e.message}")
            throw e
        }
    }

    override suspend fun getSelfBloodDonorProfile(): Flow<BloodDonorModel?> = flow {
        val userId = firebaseAuth.currentUser?.uid ?: ""
        if (userId.isEmpty()) {
            Log.d("BloodDonorRepositoryImpl", "UserId is not valid")
            return@flow
        }
        try {
            val document = firestore.collection(BLOOD_DONORS).document(userId).get().await()
            val donor = document.toObject(BloodDonorModel::class.java)
            if (userId == donor?.userId) {
                emit(donor)
            } else {
                BloodDonorModel()
            }
        } catch (e: Exception) {
            BloodDonorModel()
            Log.d("BloodDonorRepositoryImpl", "Error getting the donor profile: ${e.message}")
        }
    }


}