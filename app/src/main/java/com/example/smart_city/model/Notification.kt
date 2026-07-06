package com.example.smart_city.model

data class Notification(
    val notificationId: String = "",
    val title: String = "",
    val message: String = "",
    val type: String = "general",
    val targetUserId: String = "",
    val targetUserName: String = "",
    val targetWard: Int = 0,
    val isForAll: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val readBy: Map<String, Boolean> = emptyMap()
)