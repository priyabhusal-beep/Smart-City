package com.example.smart_city.data.models

data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val phone: String = "",
    val userType: String = "user", // "admin" or "user"
    val profilePicture: String = "",
    val address: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val isEmailVerified: Boolean = false,
    val lastLogin: Long = System.currentTimeMillis()
)