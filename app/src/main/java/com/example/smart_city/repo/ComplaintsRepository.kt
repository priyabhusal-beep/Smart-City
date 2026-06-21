package com.example.smart_city.repo

import com.example.smart_city.model.ReportModel
import com.google.firebase.database.FirebaseDatabase

class ComplaintsRepository {
    private val database = FirebaseDatabase.getInstance().getReference("Complaints")

    fun getAllComplaints(onResult: (List<ReportModel>) -> Unit) {
        database.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val complaints = mutableListOf<ReportModel>()
                for (snapshot in task.result.children) {
                    val complaint = snapshot.getValue(ReportModel::class.java)
                    if (complaint != null) complaints.add(complaint)
                }
                onResult(complaints.sortedByDescending { it.timestamp })
            } else {
                onResult(emptyList())
            }
        }
    }

    fun getComplaintsByCategory(category: String, onResult: (List<ReportModel>) -> Unit) {
        database.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val complaints = mutableListOf<ReportModel>()
                for (snapshot in task.result.children) {
                    val complaint = snapshot.getValue(ReportModel::class.java)
                    if (complaint != null && complaint.category.equals(category, ignoreCase = true)) {
                        complaints.add(complaint)
                    }
                }
                onResult(complaints.sortedByDescending { it.timestamp })
            } else {
                onResult(emptyList())
            }
        }
    }

    fun searchComplaints(query: String, onResult: (List<ReportModel>) -> Unit) {
        database.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val complaints = mutableListOf<ReportModel>()
                for (snapshot in task.result.children) {
                    val complaint = snapshot.getValue(ReportModel::class.java)
                    if (complaint != null &&
                        (complaint.area.contains(query, ignoreCase = true) ||
                                complaint.description.contains(query, ignoreCase = true) ||
                                complaint.issueType.contains(query, ignoreCase = true))
                    ) {
                        complaints.add(complaint)
                    }
                }
                onResult(complaints.sortedByDescending { it.timestamp })
            } else {
                onResult(emptyList())
            }
        }
    }
}