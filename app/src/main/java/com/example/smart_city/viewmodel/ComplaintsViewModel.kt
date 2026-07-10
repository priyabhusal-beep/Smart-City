package com.example.smart_city.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.smart_city.model.ReportModel
import com.example.smart_city.repo.ComplaintsRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ComplaintsViewModel(
    private val repository: ComplaintsRepository = ComplaintsRepository()
) : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val databaseReference =
        FirebaseDatabase.getInstance().reference

    var complaints by mutableStateOf<List<ReportModel>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf("")
        private set

    var selectedComplaint by mutableStateOf<ReportModel?>(null)
        private set

    /*
     Stores the ward number of the currently logged-in admin.
    */
    var adminWardNo by mutableStateOf(0)
        private set

    /*
     Reads the currently logged-in admin's ward number
     from Firebase and then fetches complaints for that ward.
    */
    fun loadAdminWardAndComplaints() {

        val currentAdmin = firebaseAuth.currentUser

        if (currentAdmin == null) {
            isLoading = false
            errorMessage = "Admin is not logged in."
            complaints = emptyList()
            return
        }

        isLoading = true
        errorMessage = ""

        databaseReference
            .child("users")
            .child(currentAdmin.uid)
            .child("wardNo")
            .get()
            .addOnSuccessListener { snapshot ->

                /*
                 Firebase usually returns numbers as Long.

                 This also handles wardNo stored as:
                 1
                 "1"
                */
                val wardNumber = when (val value = snapshot.value) {

                    is Long -> value.toInt()

                    is Int -> value

                    is Double -> value.toInt()

                    is String -> value.toIntOrNull() ?: 0

                    else -> 0
                }

                Log.d(
                    "ADMIN_DASHBOARD",
                    "Admin UID: ${currentAdmin.uid}"
                )

                Log.d(
                    "ADMIN_DASHBOARD",
                    "Admin Ward: $wardNumber"
                )

                if (wardNumber <= 0) {
                    adminWardNo = 0
                    complaints = emptyList()
                    isLoading = false
                    errorMessage =
                        "Ward number is not assigned to this admin account."

                    return@addOnSuccessListener
                }

                adminWardNo = wardNumber

                /*
                 After getting the admin ward,
                 fetch complaints belonging to that ward.
                */
                fetchComplaintsByWard(wardNumber)
            }
            .addOnFailureListener { exception ->

                Log.e(
                    "ADMIN_DASHBOARD",
                    "Failed to load admin ward",
                    exception
                )

                adminWardNo = 0
                complaints = emptyList()
                isLoading = false

                errorMessage =
                    exception.message
                        ?: "Unable to load the admin ward."
            }
    }

    /*
     Fetch every complaint in Firebase.
    */
    fun fetchAllComplaints() {

        isLoading = true
        errorMessage = ""

        repository.getAllComplaints { fetchedComplaints ->

            Log.d(
                "VIEWMODEL",
                "Fetched complaints = ${fetchedComplaints.size}"
            )

            complaints = fetchedComplaints.sortedByDescending {
                it.timestamp
            }

            isLoading = false
        }
    }

    /*
     Fetch complaints belonging only to the provided ward.
    */
    fun fetchComplaintsByWard(wardNo: Int) {

        if (wardNo <= 0) {
            complaints = emptyList()
            isLoading = false
            errorMessage = "Invalid ward number."
            return
        }

        isLoading = true
        errorMessage = ""

        Log.d(
            "ADMIN_DASHBOARD",
            "Fetching complaints for Ward $wardNo"
        )

        repository.getComplaintsByWard(wardNo) { fetchedComplaints ->

            Log.d(
                "ADMIN_DASHBOARD",
                "Ward $wardNo complaints fetched: ${fetchedComplaints.size}"
            )

            complaints = fetchedComplaints
                .filter { complaint ->
                    complaint.ward.trim().toIntOrNull() == wardNo
                }
                .sortedByDescending {
                    it.timestamp
                }

            isLoading = false
        }
    }

    /*
     Updates the selected complaint status.

     Suggested Firebase values:
     Pending
     In Progress
     Resolved
    */
    fun updateStatus(
        complaintId: String,
        newStatus: String,
        onComplete: (Boolean) -> Unit = {}
    ) {

        if (complaintId.isBlank()) {
            errorMessage = "Complaint ID is missing."
            onComplete(false)
            return
        }

        val formattedStatus = normalizeStatusForFirebase(
            newStatus
        )

        repository.updateComplaintStatus(
            complaintId = complaintId,
            newStatus = formattedStatus
        ) { success ->

            if (success) {

                /*
                 The repository may already update the list through
                 addValueEventListener.

                 Fetching again ensures the dashboard count updates
                 even if the repository uses a single-value listener.
                */
                if (adminWardNo > 0) {
                    fetchComplaintsByWard(adminWardNo)
                } else {
                    fetchAllComplaints()
                }

                onComplete(true)

            } else {
                errorMessage = "Unable to update complaint status."
                onComplete(false)
            }
        }
    }

    /*
     Converts different status inputs to consistent Firebase values.
    */
    private fun normalizeStatusForFirebase(
        status: String
    ): String {

        val normalizedStatus = status
            .trim()
            .lowercase()
            .replace("_", " ")
            .replace("-", " ")
            .replace(Regex("\\s+"), " ")

        return when (normalizedStatus) {

            "pending" -> "Pending"

            "in progress",
            "inprogress",
            "processing",
            "working" -> "In Progress"

            "resolved",
            "complete",
            "completed" -> "Resolved"

            else -> status.trim()
        }
    }

    /*
     Adds or removes the current user's vote.
    */
    fun toggleVote(
        complaintId: String
    ) {

        val currentUser =
            FirebaseAuth.getInstance().currentUser
                ?: return

        repository.toggleVote(
            complaintId = complaintId,
            userId = currentUser.uid
        ) {

            if (adminWardNo > 0) {
                fetchComplaintsByWard(adminWardNo)
            } else {
                fetchAllComplaints()
            }
        }
    }

    /*
     Filters complaints by category.
    */
    fun filterByCategory(
        category: String
    ): List<ReportModel> {

        return if (category.isBlank()) {
            complaints
        } else {
            complaints.filter {
                it.category.equals(
                    category,
                    ignoreCase = true
                )
            }
        }
    }

    /*
     Searches complaints by area, description,
     issue type or category.
    */
    fun searchComplaints(
        query: String
    ): List<ReportModel> {

        val searchText = query.trim()

        return if (searchText.isBlank()) {
            complaints
        } else {
            complaints.filter { complaint ->

                complaint.area.contains(
                    searchText,
                    ignoreCase = true
                ) ||
                        complaint.description.contains(
                            searchText,
                            ignoreCase = true
                        ) ||
                        complaint.issueType.contains(
                            searchText,
                            ignoreCase = true
                        ) ||
                        complaint.category.contains(
                            searchText,
                            ignoreCase = true
                        ) ||
                        complaint.status.contains(
                            searchText,
                            ignoreCase = true
                        )
            }
        }
    }

    /*
     Fetches one complaint using its Firebase ID.
    */
    fun fetchComplaintById(
        complaintId: String
    ) {

        if (complaintId.isBlank()) {
            selectedComplaint = null
            errorMessage = "Complaint ID is missing."
            return
        }

        isLoading = true
        errorMessage = ""

        repository.getComplaintById(
            complaintId
        ) { complaint ->

            selectedComplaint = complaint
            isLoading = false

            if (complaint == null) {
                errorMessage = "Complaint not found."
            }
        }
    }

    /*
     Clears the selected complaint when leaving
     the complaint details screen.
    */
    fun clearSelectedComplaint() {
        selectedComplaint = null
    }

    /*
     Clears an error after it has been displayed.
    */
    fun clearErrorMessage() {
        errorMessage = ""
    }
}