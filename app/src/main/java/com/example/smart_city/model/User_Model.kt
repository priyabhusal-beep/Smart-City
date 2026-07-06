package com.example.smart_city.model

data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val phone: String = "",
    val profilePicture: String = "",
    val userType: String = "citizen",
    val wardNo: Int = 0,
    val createdAt: Long = 0,
    val lastLogin: Long = 0
)