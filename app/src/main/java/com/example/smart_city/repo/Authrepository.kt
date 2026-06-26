package com.example.smart_city.repo

import com.example.smart_city.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    suspend fun register(
        email: String,
        password: String,
        name: String,
        phone: String
    ): User {
        val authResult = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = authResult.user?.uid ?: throw Exception("Failed to get UID")

        val user = User(
            uid = uid,
            email = email,
            name = name,
            phone = phone,
            userType = "citizen",
            wardNo = 0,
            createdAt = System.currentTimeMillis()
        )

        database.child("users").child(uid).setValue(user).await()
        return user
    }

    suspend fun login(email: String, password: String): User {
        val authResult = auth.signInWithEmailAndPassword(email, password).await()
        val uid = authResult.user?.uid ?: throw Exception("Failed to get UID")

        val snapshot = database.child("users").child(uid).get().await()
        val user = snapshot.getValue(User::class.java)
            ?: throw Exception("User data not found")

        database.child("users").child(uid).child("lastLogin")
            .setValue(System.currentTimeMillis()).await()

        return user
    }

    suspend fun getCurrentUser(): User {
        val firebaseUser = auth.currentUser ?: throw Exception("No user logged in")
        val uid = firebaseUser.uid

        val snapshot = database.child("users").child(uid).get().await()
        return snapshot.getValue(User::class.java)
            ?: throw Exception("User data not found")
    }

    fun logout() {
        auth.signOut()
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}