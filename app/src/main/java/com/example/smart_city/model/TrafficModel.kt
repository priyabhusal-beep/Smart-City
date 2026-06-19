package com.example.smart_city.model

data class TrafficModel(
    val id: String = "",
    val locationName: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val morningLevel: String = "Low",
    val afternoonLevel: String = "Low",
    val eveningLevel: String = "Low",
    val nightLevel: String = "Low",
    val jamLevel: String = ""


) {


    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "locationName" to locationName,
            "latitude" to latitude,
            "longitude" to longitude,

            )


    }
}

