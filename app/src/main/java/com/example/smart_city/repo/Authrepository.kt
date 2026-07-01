package com.example.smart_city.repo

import com.example.smart_city.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
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
            profilePicture = "",
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

    suspend fun signInWithGoogle(idToken: String, userType: String): User {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val authResult = auth.signInWithCredential(credential).await()
        val firebaseUser = authResult.user ?: throw Exception("Google sign-in failed")

        val uid = firebaseUser.uid
        val snapshot = database.child("users").child(uid).get().await()

        return if (snapshot.exists()) {
            val user = snapshot.getValue(User::class.java) ?: throw Exception("User data not found")
            database.child("users").child(uid).child("lastLogin")
                .setValue(System.currentTimeMillis()).await()
            user
        } else {
            val newUser = User(
                uid = uid,
                email = firebaseUser.email ?: "",
                name = firebaseUser.displayName ?: "",
                phone = firebaseUser.phoneNumber ?: "",
                profilePicture = firebaseUser.photoUrl?.toString() ?: "",
                userType = userType,
                wardNo = 0,
                createdAt = System.currentTimeMillis(),
                lastLogin = System.currentTimeMillis()
            )
            database.child("users").child(uid).setValue(newUser).await()
            newUser
        }
    }

    suspend fun updateProfilePicture(url: String) {
        val user = auth.currentUser ?: throw Exception("No user logged in")
        database.child("users").child(user.uid).child("profilePicture").setValue(url).await()
    }

    suspend fun updateUserProfile(name: String, email: String, password: String) {
        val user = auth.currentUser ?: throw Exception("No user logged in")

        if (name.isNotBlank()) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            user.updateProfile(profileUpdates).await()
            database.child("users").child(user.uid).child("name").setValue(name).await()
        }

        if (email.isNotBlank() && email != user.email) {
            @Suppress("DEPRECATION")
            user.updateEmail(email).await()
            database.child("users").child(user.uid).child("email").setValue(email).await()
        }

        if (password.isNotBlank()) {
            user.updatePassword(password).await()
        }
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
