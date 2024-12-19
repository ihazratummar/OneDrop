package com.hazrat.onedrop.core.domain.repository

import com.hazrat.onedrop.core.domain.model.BloodDonorModel
import com.hazrat.onedrop.core.domain.model.BloodGroup
import com.hazrat.onedrop.core.domain.model.State
import com.hazrat.onedrop.util.results.BloodDonorProfileError
import com.hazrat.onedrop.util.results.BloodDonorProfileSuccess
import com.hazrat.onedrop.util.results.Result
import com.hazrat.onedrop.util.results.SelfProfileError
import com.hazrat.onedrop.util.results.SelfProfileSuccess
import kotlinx.coroutines.flow.Flow

/**
 * @author Hazrat Ummar Shaikh
 * Created on 06-12-2024
 */

interface BloodDonorRepository {

    suspend fun createBloodDonorProfile(bloodDonorModel: BloodDonorModel): Result<BloodDonorProfileSuccess, BloodDonorProfileError>
    suspend fun updateBloodDonorProfile(bloodDonorModel: BloodDonorModel): Result<SelfProfileSuccess, SelfProfileError>

    suspend fun getListOfDonorsWithoutCurrentUser(
        userId: String,
        bloodGroup: BloodGroup? = null,
        state: State? = null
    ): Flow<List<BloodDonorModel>>

    suspend fun isBloodDonorProfileExists(userId: String): Flow<Boolean>

    suspend fun getListOfAllDonors(): Flow<List<BloodDonorModel>>

    suspend fun getBloodDonorProfile(userId: String): Flow<BloodDonorModel>

    suspend fun getSelfBloodDonorProfile(): Flow<BloodDonorModel>


}