package com.hazrat.onedrop.core.presentation.blood_donor_profile_details_screen

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.net.Uri
import com.hazrat.onedrop.core.domain.model.BloodDonorModel
import com.hazrat.onedrop.core.domain.repository.BloodDonorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 * Created on 09-12-2024
 */


@HiltViewModel
class BloodDonorProfileViewModel @Inject constructor(
    private val bloodDonorRepository: BloodDonorRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {


    private val _bloodDonorModel = MutableStateFlow(BloodDonorModel())
    val bloodDonorModel: StateFlow<BloodDonorModel> = _bloodDonorModel.asStateFlow()


    fun getBloodDonorProfile(userId: String) {
        viewModelScope.launch {
            bloodDonorRepository.getBloodDonorProfile(userId).collect {
                delay(1000)
                _bloodDonorModel.value = it
            }

        }
    }

    fun onEvent(bloodDonorProfileEvent: BloodDonorProfileEvent) {
        when (bloodDonorProfileEvent) {
            BloodDonorProfileEvent.CallNow -> {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${bloodDonorModel.value.contactNumber}")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            }
        }
    }

}