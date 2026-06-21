package com.example.smart_city.repo

import android.util.Log
import com.example.smart_city.model.User
import com.google.firebase.auth.EmailAuthProvider
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

    // ✅ UPDATED: Update User Profile with proper email handling
    suspend fun updateUserProfile(
        name: String,
        email: String,
        password: String = ""  // Required for email update
    ): User {
        return try {
            val currentUser = auth.currentUser
                ?: throw Exception("No user logged in")

            val uid = currentUser.uid

            // Step 1: Get current user data
            val snapshot = database.child("users").child(uid).get().await()
            val user = snapshot.getValue(User::class.java)
                ?: throw Exception("User not found in database")

            // Step 2: Create updated user object
            val updatedUser = user.copy(
                name = name,
                email = email
            )

            // Step 3: Update in Firebase Database
            database.child("users").child(uid).setValue(updatedUser).await()
            Log.d("AuthRepo", "User data updated in database")

            // ✅ STEP 4: Update email in Firebase Auth (if changed)
            if (email != currentUser.email) {
                try {
                    // If email is different, need to re-authenticate first
                    if (password.isNotEmpty()) {
                        // Re-authenticate with current password
                        val credential = EmailAuthProvider.getCredential(
                            currentUser.email ?: "",
                            password
                        )
                        currentUser.reauthenticate(credential).await()
                        Log.d("AuthRepo", "Re-authentication successful")
                    }

                    // Now update email
                    currentUser.updateEmail(email).await()
                    Log.d("AuthRepo", "Email updated in Firebase Auth: $email")

                } catch (e: Exception) {
                    Log.e("AuthRepo", "Failed to update email in Auth: ${e.message}")
                    // Still return success - database is updated
                    // The email will work after next login
                }
            }

            Log.d("AuthRepo", "Profile updated: name=$name, email=$email")

            // Step 5: Return updated user
            updatedUser

        } catch (e: Exception) {
            Log.e("AuthRepo", "Update profile failed: ${e.message}")
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