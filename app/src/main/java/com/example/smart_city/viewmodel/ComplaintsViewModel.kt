package com.example.smart_city.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.smart_city.model.ReportModel
import com.example.smart_city.repo.ComplaintsRepository

class ComplaintsViewModel(
    private val repository: ComplaintsRepository = ComplaintsRepository()
) : ViewModel() {

    var complaints by mutableStateOf(listOf<ReportModel>())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    fun fetchAllComplaints() {
        isLoading = true
        errorMessage = ""

        repository.getAllComplaints { fetchedComplaints ->
            complaints = fetchedComplaints
            isLoading = false
        }
    }

    fun fetchComplaintsByWard(wardNo: Int) {
        isLoading = true
        errorMessage = ""

        repository.getComplaintsByWard(wardNo) { fetchedComplaints ->
            complaints = fetchedComplaints
            isLoading = false
        }
    }

    fun filterByCategory(category: String): List<ReportModel> {
        return if (category.isEmpty()) {
            complaints
        } else {
            complaints.filter {
                it.category.equals(category, ignoreCase = true)
            }
        }
    }

    fun searchComplaints(query: String): List<ReportModel> {
        return if (query.isEmpty()) {
            complaints
        } else {
            complaints.filter {
                it.area.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true) ||
                        it.issueType.contains(query, ignoreCase = true)
            }
        }
    }
}