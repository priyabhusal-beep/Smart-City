package com.example.smart_city

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ReportViewModel(private val repository: ReportRepository = ReportRepository()) : ViewModel() {
    var searchArea by mutableStateOf("")
    var description by mutableStateOf("")
    var ward by mutableStateOf("")
    var issueType by mutableStateOf("")

    // For the Searchable Area logic
    val areaSuggestions = listOf("Baneshwor", "Kalanki", "Koteshwor", "Patan", "Thamel") // Add more or fetch from API

    fun getFilteredAreas(): List<String> {
        return if (searchArea.isEmpty()) emptyList()
        else areaSuggestions.filter { it.contains(searchArea, ignoreCase = true) }
    }

    fun submit(category: String, onResult: (String) -> Unit) {
        if (searchArea.isEmpty() || description.isEmpty() || ward.isEmpty()) {
            onResult("Please fill all fields")
            return
        }

        val report = ReportModel(category, ward, issueType, searchArea, description)
        repository.submitReport(report) { success ->
            if (success) onResult("Report Submitted Successfully")
            else onResult("Failed to Submit")
        }
    }
}