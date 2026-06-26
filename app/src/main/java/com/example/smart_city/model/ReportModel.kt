package com.example.smart_city.model

data class ReportModel(
    val id: String = "",
    val category: String = "",
    val ward: String = "",
    val wardNo: Int = 0,
    val issueType: String = "",
    val area: String = "",
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val userId: String = "",
    val status: String = "Pending"
)