package com.hazrat.onedrop.core.domain.model.district_model

data class DistrictListModelItem(
    val area: Int,
    val density: Int,
    val district: String,
    val districtCode: String,
    val headquarters: String,
    val population: Int,
    val state: String,
    val stateCode: String
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            district,
            "${district.first()}",
            districtCode
        )
        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}