package com.example.smart_city.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class TrafficModel(
    var id: String = "",
    var locationName: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var morningLevel: String = "Low",
    var afternoonLevel: String = "Low",
    var eveningLevel: String = "Low",
    var nightLevel: String = "Low",
    var jamLevel: String = ""
) {

    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "locationName" to locationName,
            "latitude" to latitude,
            "longitude" to longitude,
            "morningLevel" to morningLevel,
            "afternoonLevel" to afternoonLevel,
            "eveningLevel" to eveningLevel,
            "nightLevel" to nightLevel,
            "jamLevel" to jamLevel
        )
    }
}
