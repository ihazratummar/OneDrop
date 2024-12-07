package com.hazrat.onedrop.core.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hazrat.onedrop.auth.presentation.UserData
import com.hazrat.onedrop.core.domain.model.BloodDonorModel
import com.hazrat.onedrop.core.domain.model.BloodGroup
import com.hazrat.onedrop.core.domain.model.Location
import com.hazrat.onedrop.core.domain.model.State
import com.hazrat.onedrop.core.domain.repository.BloodDonorRepository
import com.hazrat.onedrop.util.datastore.DataStorePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
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

    private val _bloodDonorProfile = MutableStateFlow<BloodDonorModel?>(null)
    override val bloodDonorProfile: StateFlow<BloodDonorModel?> = _bloodDonorProfile.asStateFlow()

    private val userId = firebaseAuth.currentUser?.uid ?: ""

    override suspend fun createBloodDonorProfile() {
        try {
            val userData = firestore.collection("users").document(this.userId).get().await()
            val donor = userData.toObject(UserData::class.java)
            val bloodDonorModel = BloodDonorModel(
                userId = this.userId,
                name = _bloodDonorProfile.value?.name ?: donor?.fullName ?: "Guest user",
                bloodGroup = _bloodDonorProfile.value?.bloodGroup ?: BloodGroup.O_POSITIVE,
                district = _bloodDonorProfile.value?.district ?: "Murshidabad",
                state = _bloodDonorProfile.value?.state ?: State.WEST_BENGAL,
                available = _bloodDonorProfile.value?.available ?: true,
                contactNumber = _bloodDonorProfile.value?.contactNumber ?: "76566474xxx",
                isContactNumberPrivate = _bloodDonorProfile.value?.isContactNumberPrivate ?: true,
                notificationEnabled = _bloodDonorProfile.value?.notificationEnabled ?: true,
                notificationScope = _bloodDonorProfile.value?.notificationScope ?: Location.DISTRICT
            )
            firestore.collection("blood_donors").document(this.userId).set(bloodDonorModel)
                .addOnSuccessListener {
                    Log.d("BloodDonorRepositoryImpl", "createBloodDonorProfile: Success")
                    _bloodDonorProfile.update {
                        it?.copy(
                            name = bloodDonorModel.name,
                            bloodGroup = bloodDonorModel.bloodGroup,
                            district = bloodDonorModel.district,
                            state = bloodDonorModel.state,
                            available = bloodDonorModel.available,
                            contactNumber = bloodDonorModel.contactNumber,
                            isContactNumberPrivate = bloodDonorModel.isContactNumberPrivate,
                            notificationEnabled = bloodDonorModel.notificationEnabled,
                            notificationScope = bloodDonorModel.notificationScope
                        )
                    }
                }
                .addOnFailureListener {
                    Log.d("BloodDonorRepositoryImpl", "createBloodDonorProfile: ${it.message}")
                }

        } catch (e: Exception) {
            Log.d("BloodDonorRepositoryImpl", "createBloodDonorProfile: ${e.message}")
        }
    }

    override fun setName(name: String) {
        _bloodDonorProfile.update {
            it?.copy(name = name)
        }
    }

    override fun setBloodGroup(bloodGroup: BloodGroup) {
        _bloodDonorProfile.update {
            it?.copy(
                bloodGroup = bloodGroup
            )
        }
    }

    override fun setDistrict(district: String) {
        _bloodDonorProfile.update {
            it?.copy(
                district = district
            )
        }
    }

    override fun setState(state: State) {
        _bloodDonorProfile.update {
            it?.copy(
                state = state
            )
        }
    }

    override fun setAvailable() {
        _bloodDonorProfile.update {
            it?.copy(
                available = !it.available
            )
        }
    }

    override fun setContactNumber(number: String) {
        _bloodDonorProfile.update {
            it?.copy(
                contactNumber = number
            )
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