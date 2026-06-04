package com.example.smart_city.data.repository

import com.example.smart_city.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    // REGISTER - Create new user
    suspend fun register(
        email: String,
        password: String,
        name: String,
        userType: String
    ): User {
        return try {
            // Step 1: Create user in Firebase Auth
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("Failed to get UID")

            // Step 2: Create User object
            val user = User(
                uid = uid,
                email = email,
                name = name,
                userType = userType,
                createdAt = System.currentTimeMillis()
            )

            // Step 3: Save user data to Database
            database.child("users").child(uid).setValue(user).await()

            // Step 4: Return the user
            user

        } catch (e: Exception) {
            throw e
        }
    }

    // LOGIN - Authenticate user
    suspend fun login(email: String, password: String): User {
        return try {
            // Step 1: Authenticate with Firebase Auth
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("Failed to get UID")

            // Step 2: Fetch user data from Database
            val snapshot = database.child("users").child(uid).get().await()
            val user = snapshot.getValue(User::class.java)
                ?: throw Exception("User not found in database")

            // Step 3: Return success with user object
            user

        } catch (e: Exception) {
            throw e
        }
    }

    // Get Current Logged-in User
    suspend fun getCurrentUser(): User {
        return try {
            val currentUser = auth.currentUser
                ?: throw Exception("No user logged in")

            val uid = currentUser.uid
            val snapshot = database.child("users").child(uid).get().await()
            val user = snapshot.getValue(User::class.java)
                ?: throw Exception("User not found in database")

            user

        } catch (e: Exception) {
            throw e
        }
    }

    // Logout Function
    fun logout() {
        auth.signOut()
    }

    // Check if User is Already Logged In
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // Get Current User UID
    fun getCurrentUserUID(): String? {
        return auth.currentUser?.uid
    }
}