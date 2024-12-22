package com.hazrat.onedrop.core.domain.usecase

import com.hazrat.onedrop.core.domain.model.cities_model.CitiesModelItem

/**
 * @author Hazrat Ummar Shaikh
 * Created on 20-12-2024
 */

class CityValidation {

    operator fun invoke(
        city: String,
        citiesModel: List<CitiesModelItem>
    ): Boolean {
        return city.isNotBlank() && citiesModel.any {
            it.city.equals(city, ignoreCase = true)
        }
    }

}