package com.hazrat.onedrop.core.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hazrat.onedrop.core.domain.model.BloodDonorModel
import com.hazrat.onedrop.core.domain.repository.BloodDonorRepository
import com.hazrat.onedrop.util.datastore.DataStorePreference
import com.hazrat.onedrop.util.results.BloodDonorProfileError
import com.hazrat.onedrop.util.results.BloodDonorProfileSuccess
import com.hazrat.onedrop.util.results.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 * Created on 06-12-2024
 */

class BloodDonorRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val dataStorePreference: DataStorePreference
) : BloodDonorRepository {


    private val userId = firebaseAuth.currentUser?.uid ?: ""

    override suspend fun createBloodDonorProfile(bloodDonorModel: BloodDonorModel): Result<BloodDonorProfileSuccess, BloodDonorProfileError> {
        try {
            val userData = firestore.collection("users").document(this.userId).get().await()
            val bloodDonorModel = bloodDonorModel
            val result = firestore.collection("blood_donors").document(this.userId).set(bloodDonorModel).await()
            return if (result != null) {
                Result.Success(BloodDonorProfileSuccess.SUCCESS)
            } else {
                Result.Error(BloodDonorProfileError.FAILED)
            }
        } catch (e: Exception) {
            return return Result.Error(BloodDonorProfileError.FAILED)
        }
    }


    override suspend fun getListOfDonors(): Flow<List<BloodDonorModel>> = flow{
        try {
            val donorsList = mutableListOf<BloodDonorModel>()
            val querySnapshot = firestore.collection("blood_donors").get().await()

            for (document in querySnapshot.documents) {
                val donor = document.toObject(BloodDonorModel::class.java)
                donor?.let { donorsList.add(it) }
            }
            Log.d("BloodDonorRepositoryImpl", "getting the list: ${donorsList.size}")
            emit(donorsList) // Emit the list of donors
        } catch (e: Exception) {
            Log.d("BloodDonorRepositoryImpl", "Error getting the list: ${e.message}")
            emit(emptyList()) // Emit an empty list in case of an error
        }
    }

    override suspend fun isBloodDonorProfileExists(): Flow<Boolean> = flow {
        // Ensure that userId is not null or empty
        if (this@BloodDonorRepositoryImpl.userId.isEmpty()) {
            Log.d("BloodDonorRepositoryImpl", "UserId is not valid")
//            dataStorePreference.setBloodDonorRegistered(false)
            emit(false) // Emit false if userId is not valid
            return@flow
        }

        try {
            val donorProfile = firestore.collection("blood_donors").document(this@BloodDonorRepositoryImpl.userId).get().await()
            val donor = donorProfile.toObject(BloodDonorModel::class.java)

            // Update DataStore preference based on the existence of the donor profile
            if (donor?.userId == this@BloodDonorRepositoryImpl.userId) {
//                dataStorePreference.setBloodDonorRegistered(true)
                emit(true) // Emit true if the donor profile exists
            } else {
//                dataStorePreference.setBloodDonorRegistered(false)
                emit(false) // Emit false if the donor profile does not exist
            }
        } catch (e: Exception) {
            Log.d("BloodDonorRepositoryImpl", "Error checking if donor profile exists: ${e.message}")
            emit(false) // Emit false in case of an error
        }
    }
}