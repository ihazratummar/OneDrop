package com.hazrat.onedrop.core.domain.repository

import com.hazrat.onedrop.core.domain.model.BloodDonorModel
import com.hazrat.onedrop.core.domain.model.BloodGroup
import com.hazrat.onedrop.core.domain.model.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * @author Hazrat Ummar Shaikh
 * Created on 06-12-2024
 */

interface BloodDonorRepository {

    val bloodDonorProfile: StateFlow<BloodDonorModel?>

    suspend fun createBloodDonorProfile()

    fun setName(name: String)

    fun setBloodGroup(bloodGroup: BloodGroup)

    fun setDistrict(district: String)

    fun setState(state: State)

    fun setAvailable()

    fun setContactNumber(number: String)

    suspend fun getListOfDonors(): Flow<List<BloodDonorModel>>

    suspend fun isBloodDonorProfileExists(): Flow<Boolean>


}