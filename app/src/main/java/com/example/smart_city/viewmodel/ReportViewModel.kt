package com.example.smart_city.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.smart_city.model.ReportModel
import com.example.smart_city.repo.ReportRepository
import com.google.firebase.auth.FirebaseAuth

open class ReportViewModel(private val repository: ReportRepository = ReportRepository()) : ViewModel() {
    var searchArea by mutableStateOf("")
    var description by mutableStateOf("")
    var ward by mutableStateOf("")
    var issueType by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    
    // IMAGE STATES
    var capturedImage by mutableStateOf<Bitmap?>(null)
    var imageUrl by mutableStateOf("")

    var userComplaints by mutableStateOf<List<ReportModel>>(emptyList())
    var totalUserVotes by mutableStateOf(0)

    var latitude by mutableStateOf(0.0)
    var longitude by mutableStateOf(0.0)

    val areaSuggestions = listOf("Baneshwor", "Kalanki", "Koteshwor", "Patan", "Thamel", "Maitidevi", "Baluwatar")

    fun getFilteredAreas(): List<String> {
        return if (searchArea.isEmpty()) emptyList()
        else areaSuggestions.filter { it.contains(searchArea, ignoreCase = true) }
    }

    fun fetchUserComplaints() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        isLoading = true
        repository.getUserComplaints(userId) { complaints ->
            userComplaints = complaints
            isLoading = false
        }
    }
    fun fetchTotalUserVotes() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        repository.getAllComplaints { complaints ->

            totalUserVotes = complaints.count { complaint ->
                complaint.votes.containsKey(currentUserId)
            }

        }
    }

    open fun submit(category: String, onResult: (String) -> Unit) {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser == null) {
            onResult("❌ ERROR: NOT LOGGED IN - Please login first!")
            return
        }

        // Validate fields
        if (ward.isEmpty()) {
            onResult("❌ Please select a Ward!")
            return
        }
        if (issueType.isEmpty()) {
            onResult("❌ Please select an Issue Type!")
            return
        }
        if (searchArea.isEmpty()) {
            onResult("❌ Please enter Area/Locality!")
            return
        }
        if (description.isEmpty()) {
            onResult("❌ Please describe the issue!")
            return
        }

        if (latitude == 0.0 || longitude == 0.0) {
            onResult("❌ Please detect location first!")
            return
        }

        isLoading = true

        // Create report model including the imageUrl
        val report = ReportModel(
            id = System.currentTimeMillis().toString(),
            category = category,
            ward = ward,
            issueType = issueType,
            area = searchArea,
            description = description,
            timestamp = System.currentTimeMillis(),
            userId = currentUser.uid,
            status = "pending",
            latitude = latitude,
            longitude = longitude,
            imageUrl = imageUrl
        )

        // Submit to Firebase
        repository.submitReport(report) { success ->
            isLoading = false
            if (success) {
                onResult("✅ Report Submitted Successfully!")
                resetForm()
            } else {
                onResult("❌ Failed to Submit - Try Again!")
            }
        }
    }

    fun resetForm() {
        ward = ""
        issueType = ""
        searchArea = ""
        description = ""
        capturedImage = null
        imageUrl = ""
    }
}
