package com.hazrat.onedrop.core.domain.repository

import com.hazrat.onedrop.core.domain.model.BloodDonorModel
import com.hazrat.onedrop.util.results.BloodDonorProfileError
import com.hazrat.onedrop.util.results.BloodDonorProfileSuccess
import com.hazrat.onedrop.util.results.Result
import kotlinx.coroutines.flow.Flow

/**
 * @author Hazrat Ummar Shaikh
 * Created on 06-12-2024
 */

interface BloodDonorRepository {

    suspend fun createBloodDonorProfile(bloodDonorModel: BloodDonorModel) : Result<BloodDonorProfileSuccess , BloodDonorProfileError>

    suspend fun getListOfDonors(): Flow<List<BloodDonorModel>>

    suspend fun isBloodDonorProfileExists(): Flow<Boolean>


}