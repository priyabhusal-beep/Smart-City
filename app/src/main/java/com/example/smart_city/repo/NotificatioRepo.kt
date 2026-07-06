package com.example.smart_city.repo

import com.example.smart_city.model.Notification
import com.example.smart_city.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import android.util.Log
class NotificationRepo {

    private val database = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    suspend fun sendNotification(notification: Notification) {
        val ref = database.child("notifications").push()
        val id = ref.key ?: throw Exception("Failed to create notification ID")

        ref.setValue(notification.copy(notificationId = id)).await()
    }



    suspend fun getAllUsers(): List<User> {

        val snapshot = database.child("users").get().await()

        Log.d("USER_DEBUG", "Children = ${snapshot.childrenCount}")

        snapshot.children.forEach {
            Log.d("USER_DEBUG", it.value.toString())
        }

        return snapshot.children.mapNotNull {
            try {
                it.getValue(User::class.java)
            } catch (e: Exception) {
                Log.e("USER_DEBUG", e.message.toString())
                null
            }
        }
    }

    suspend fun getUserNotifications(userWard: Int): List<Notification> {
        val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
        val snapshot = database.child("notifications").get().await()

        return snapshot.children.mapNotNull { child ->
            try {
                child.getValue(Notification::class.java)
            } catch (e: Exception) {
                null
            }
        }.filter { notification ->
            notification.isForAll ||
                    notification.targetUserId == uid ||
                    (notification.targetWard > 0 && notification.targetWard == userWard)
        }.sortedByDescending { it.createdAt }
    }

    suspend fun getUnreadNotificationCount(userWard: Int): Int {
        val uid = auth.currentUser?.uid ?: return 0
        val notifications = getUserNotifications(userWard)

        return notifications.count { notification ->
            notification.readBy[uid] != true
        }
    }

    suspend fun markAllAsRead(notifications: List<Notification>) {
        val uid = auth.currentUser?.uid ?: return

        notifications.forEach { notification ->
            if (notification.notificationId.isNotBlank()) {
                database.child("notifications")
                    .child(notification.notificationId)
                    .child("readBy")
                    .child(uid)
                    .setValue(true)
                    .await()
            }
        }
    }
}