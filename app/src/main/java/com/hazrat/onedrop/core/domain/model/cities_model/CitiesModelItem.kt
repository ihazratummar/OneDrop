package com.hazrat.onedrop.core.domain.model.cities_model

data class CitiesModelItem(
    val accentcity: String,
    val city: String,
    val latitude: String,
    val longitude: String,
    val population: String,
    val region: String
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            city,
            "${city.first()}",
        )
        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}