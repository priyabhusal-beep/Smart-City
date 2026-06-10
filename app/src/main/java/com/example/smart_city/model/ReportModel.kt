package com.example.smart_city

data class ReportModel(val category: String = "",
                       val ward: String = "",
                       val issueType: String = "",
                       val area: String = "",
                       val description: String = "",
                       val timestamp: Long = System.currentTimeMillis()
)