package com.example.smart_city.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smart_city.model.Notification
import com.example.smart_city.model.User
import com.example.smart_city.repo.NotificationRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationViewModel : ViewModel() {

    private val repo = NotificationRepo()

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount

    private val _allUsers = MutableStateFlow<List<User>>(emptyList())
    val allUsers: StateFlow<List<User>> = _allUsers

    fun loadAllUsers() {
        viewModelScope.launch {
            try {
                _allUsers.value = repo.getAllUsers()
            } catch (e: Exception) {
                _message.value = e.message ?: "Failed to load users"
            }
        }
    }

    fun sendToAll(title: String, message: String) {
        sendNotification(
            Notification(
                title = title,
                message = message,
                targetWard = -1,
                isForAll = true,
                type = "general"
            )
        )
    }

    fun sendToSpecificUser(title: String, message: String, user: User) {
        sendNotification(
            Notification(
                title = title,
                message = message,
                targetUserId = user.uid,
                targetUserName = user.name,
                targetWard = -1,
                isForAll = false,
                type = "personal"
            )
        )
    }

    private fun sendNotification(notification: Notification) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repo.sendNotification(notification)
                _message.value = "Notification sent successfully"
            } catch (e: Exception) {
                _message.value = e.message ?: "Failed to send notification"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadUserNotifications(userWard: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _notifications.value = repo.getUserNotifications(userWard)
            } catch (e: Exception) {
                _message.value = e.message ?: "Failed to load notifications"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadUnreadCount(userWard: Int) {
        viewModelScope.launch {
            try {
                _unreadCount.value = repo.getUnreadNotificationCount(userWard)
            } catch (e: Exception) {
                _unreadCount.value = 0
            }
        }
    }

    fun markAllAsRead(userWard: Int) {
        viewModelScope.launch {
            try {
                repo.markAllAsRead(_notifications.value)
                loadUnreadCount(userWard)
            } catch (e: Exception) {
                _message.value = e.message ?: "Failed to mark as read"
            }
        }
    }

    fun clearMessage() {
        _message.value = ""
    }
}