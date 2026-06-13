package com.example.smart_city.repo

import com.example.smart_city.model.ReportModel
import com.google.firebase.database.FirebaseDatabase

class ReportRepository {
    private val database = FirebaseDatabase.getInstance().getReference("Complaints")

    fun submitReport(report: ReportModel, onComplete: (Boolean) -> Unit) {
        val id = database.push().key ?: return
        database.child(id).setValue(report)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    fun getAllComplaints(onResult: (List<ReportModel>) -> Unit) {
        database.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val complaints = mutableListOf<ReportModel>()
                for (snapshot in task.result.children) {
                    val complaint = snapshot.getValue(ReportModel::class.java)
                    if (complaint != null) complaints.add(complaint)
                }
                onResult(complaints)
            } else {
                onResult(emptyList())
            }
        }
    }
}