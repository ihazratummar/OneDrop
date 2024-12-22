package com.hazrat.onedrop.core.domain.model.district_model

import android.content.Context
import com.google.gson.Gson

class DistrictListModel : ArrayList<DistrictListModelItem>() {


    fun getDistrictList(context: Context): List<DistrictListModelItem> {
        val jsonString = context.assets.open("district.json").bufferedReader().use { it.readText() }
        val districtList = Gson().fromJson(jsonString, DistrictListModel::class.java)
        return districtList
    }

}