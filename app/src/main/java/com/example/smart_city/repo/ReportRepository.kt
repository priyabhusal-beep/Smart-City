package com.example.smart_city

import com.example.smart_city.model.ReportModel
import com.google.firebase.database.FirebaseDatabase

class ReportRepository {
    private val database = FirebaseDatabase.getInstance().getReference("Complaints")

    fun submitReport(report: ReportModel, onComplete: (Boolean) -> Unit) {
        val id = database.push().key ?: return
        database.child(id).setValue(report)
            .addOnCompleteListener { onComplete(it.isSuccessful) }
    }
}