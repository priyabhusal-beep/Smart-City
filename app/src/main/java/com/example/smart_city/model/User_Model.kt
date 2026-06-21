package com.example.smart_city.model

data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val phone: String = "",
    val userType: String = "user",
    val profilePicture: String = "",
    val address: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val isEmailVerified: Boolean = false,
    val lastLogin: Long = System.currentTimeMillis()
)