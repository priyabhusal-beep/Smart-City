package com.example.smart_city.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.smart_city.model.ReportModel
import com.example.smart_city.repo.ReportRepository
import com.google.firebase.auth.FirebaseAuth

open class ReportViewModel(
    private val repository: ReportRepository = ReportRepository()
) : ViewModel() {

    var searchArea by mutableStateOf("")
    var description by mutableStateOf("")
    var ward by mutableStateOf("")
    var issueType by mutableStateOf("")
    var isLoading by mutableStateOf(false)

    val areaSuggestions = listOf(
        "Baneshwor",
        "Kalanki",
        "Koteshwor",
        "Patan",
        "Thamel",
        "Maitidevi",
        "Baluwatar"
    )

    fun getFilteredAreas(): List<String> {
        return if (searchArea.isEmpty()) {
            emptyList()
        } else {
            areaSuggestions.filter {
                it.contains(searchArea, ignoreCase = true)
            }
        }
    }

    open fun submit(category: String, onResult: (String) -> Unit) {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser == null) {
            onResult("Please login first!")
            return
        }

        if (ward.isBlank()) {
            onResult("Please select a Ward!")
            return
        }

        if (issueType.isBlank()) {
            onResult("Please select an Issue Type!")
            return
        }

        if (searchArea.isBlank()) {
            onResult("Please enter Area!")
            return
        }

        if (description.isBlank()) {
            onResult("Please enter Description!")
            return
        }

        isLoading = true

        val wardNumber = ward.filter { it.isDigit() }.toIntOrNull() ?: 0

        val report = ReportModel(
            category = category,
            ward = ward,
            wardNo = wardNumber,
            issueType = issueType,
            area = searchArea,
            description = description,
            timestamp = System.currentTimeMillis(),
            userId = currentUser.uid,
            status = "Pending"
        )

        repository.submitReport(report) { success ->
            isLoading = false

            if (success) {
                onResult("Report Submitted Successfully!")
                resetForm()
            } else {
                onResult("Failed to submit report!")
            }
        }
    }

    fun resetForm() {
        ward = ""
        issueType = ""
        searchArea = ""
        description = ""
    }
}
