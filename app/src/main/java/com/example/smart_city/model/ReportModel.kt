package com.example.smart_city.model

data class ReportModel(
    val category: String = "",
    val ward: String = "",
    val issueType: String = "",
    val area: String = "",
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val userId: String = "",
    val status: String = "Pending",
    val id: String = "",
    val latitude: Double = 0.0,
    val longitude : Double=0.0,
)