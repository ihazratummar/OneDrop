package com.hazrat.onedrop.core.domain.model.cities_model

import android.content.Context
import com.google.gson.Gson

class CitiesModel : ArrayList<CitiesModelItem>() {


    fun getCitiesModelItem(context: Context): List<CitiesModelItem> {
        val jsonString = context.assets.open("cities.json").bufferedReader().use { it.readText() }
        val localCitiesDataItem = Gson().fromJson(jsonString, CitiesModel::class.java)
        return localCitiesDataItem
    }
}