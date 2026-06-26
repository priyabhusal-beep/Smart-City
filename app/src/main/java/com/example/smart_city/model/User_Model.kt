package com.example.smart_city.model

data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val phone: String = "",
    val userType: String = "citizen", // citizen or admin
    val wardNo: Int = 0,              // 0 for citizen, 1-6 for ward admin
    val createdAt: Long = System.currentTimeMillis(),
    val lastLogin: Long = System.currentTimeMillis()
)