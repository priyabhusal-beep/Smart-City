package com.example.smart_city.repo

import android.util.Log
import com.example.smart_city.model.ReportModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener

class ComplaintsRepository {

    private val database =
        FirebaseDatabase.getInstance()
            .getReference("Complaints")

    fun getAllComplaints(
        onResult: (List<ReportModel>) -> Unit
    ) {
        database.addValueEventListener(
            object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val complaints = mutableListOf<ReportModel>()

                    for (child in snapshot.children) {
                        val complaint = child.getValue(ReportModel::class.java)

                        if (complaint != null) {
                            val complaintWithId = complaint.copy(
                                id = complaint.id.ifBlank {
                                    child.key ?: ""
                                }
                            )

                            complaints.add(complaintWithId)
                        }
                    }

                    onResult(
                        complaints.sortedByDescending {
                            it.timestamp
                        }
                    )
                }

                override fun onCancelled(error: DatabaseError) {
                    onResult(emptyList())
                }
            }
        )
    }

    fun updateComplaintStatus(
        complaintId: String,
        newStatus: String,
        onComplete: (Boolean) -> Unit
    ) {
        if (complaintId.isBlank()) {
            Log.e(
                "COMPLAINT_REPOSITORY",
                "Complaint ID is empty."
            )

            onComplete(false)
            return
        }

        database
            .child(complaintId)
            .child("status")
            .setValue(newStatus)
            .addOnCompleteListener { task ->

                Log.d(
                    "COMPLAINT_REPOSITORY",
                    "Status update success = ${task.isSuccessful}"
                )

                onComplete(task.isSuccessful)
            }
    }

    fun getComplaintsByCategory(
        category: String,
        onResult: (List<ReportModel>) -> Unit
    ) {
        database.get()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val complaints =
                        mutableListOf<ReportModel>()

                    for (snapshot in task.result.children) {

                        val complaint =
                            snapshot.getValue(
                                ReportModel::class.java
                            )

                        if (
                            complaint != null &&
                            complaint.category.equals(
                                category,
                                ignoreCase = true
                            )
                        ) {
                            val complaintWithId =
                                complaint.copy(
                                    id = complaint.id.ifBlank {
                                        snapshot.key ?: ""
                                    }
                                )

                            complaints.add(
                                complaintWithId
                            )
                        }
                    }

                    onResult(
                        complaints.sortedWith(
                            compareByDescending<ReportModel> {
                                it.voteCount
                            }.thenByDescending {
                                it.timestamp
                            }
                        )
                    )
                } else {
                    onResult(emptyList())
                }
            }
    }

    fun toggleVote(
        complaintId: String,
        userId: String,
        onComplete: () -> Unit
    ) {
        if (complaintId.isBlank()) {
            onComplete()
            return
        }

        val complaintRef =
            database.child(complaintId)

        complaintRef.runTransaction(
            object : Transaction.Handler {

                override fun doTransaction(
                    currentData: MutableData
                ): Transaction.Result {

                    val complaint =
                        currentData.getValue(
                            ReportModel::class.java
                        ) ?: return Transaction.success(
                            currentData
                        )

                    val votes =
                        complaint.votes.toMutableMap()

                    var voteCount =
                        complaint.voteCount

                    if (votes.containsKey(userId)) {
                        votes.remove(userId)

                        voteCount =
                            (voteCount - 1).coerceAtLeast(0)
                    } else {
                        votes[userId] = true
                        voteCount++
                    }

                    currentData
                        .child("votes")
                        .value = votes

                    currentData
                        .child("voteCount")
                        .value = voteCount

                    return Transaction.success(
                        currentData
                    )
                }

                override fun onComplete(
                    error: DatabaseError?,
                    committed: Boolean,
                    currentData: DataSnapshot?
                ) {
                    onComplete()
                }
            }
        )
    }

    fun getComplaintsByWard(
        wardNo: Int,
        onResult: (List<ReportModel>) -> Unit
    ) {
        /*
         We read all complaints and support both:

         wardNo: 1
         ward: "1"
         ward: "Ward 1"

         This is useful because your model currently contains
         both ward and wardNo fields.
        */
        database.addValueEventListener(
            object : ValueEventListener {

                override fun onDataChange(
                    snapshot: DataSnapshot
                ) {
                    val complaintList =
                        mutableListOf<ReportModel>()

                    for (
                    complaintSnapshot
                    in snapshot.children
                    ) {
                        val complaint =
                            complaintSnapshot.getValue(
                                ReportModel::class.java
                            )

                        if (complaint != null) {

                            val wardFromString =
                                complaint.ward
                                    .replace(
                                        "Ward",
                                        "",
                                        ignoreCase = true
                                    )
                                    .trim()
                                    .toIntOrNull()

                            val matchesWard =
                                complaint.wardNo == wardNo ||
                                        wardFromString == wardNo

                            if (matchesWard) {

                                val complaintWithId =
                                    complaint.copy(
                                        id = complaint.id.ifBlank {
                                            complaintSnapshot.key
                                                ?: ""
                                        }
                                    )

                                complaintList.add(
                                    complaintWithId
                                )
                            }
                        }
                    }

                    Log.d(
                        "COMPLAINT_REPOSITORY",
                        "Ward $wardNo complaints = ${complaintList.size}"
                    )

                    onResult(
                        complaintList.sortedByDescending {
                            it.timestamp
                        }
                    )
                }

                override fun onCancelled(
                    error: DatabaseError
                ) {
                    Log.e(
                        "COMPLAINT_REPOSITORY",
                        "Ward complaint error: ${error.message}"
                    )

                    onResult(emptyList())
                }
            }
        )
    }

    fun getComplaintById(
        complaintId: String,
        onResult: (ReportModel?) -> Unit
    ) {
        if (complaintId.isBlank()) {
            onResult(null)
            return
        }

        database
            .child(complaintId)
            .get()
            .addOnSuccessListener { snapshot ->

                val complaint =
                    snapshot.getValue(
                        ReportModel::class.java
                    )

                if (complaint != null) {
                    onResult(
                        complaint.copy(
                            id = complaint.id.ifBlank {
                                snapshot.key ?: ""
                            }
                        )
                    )
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener { exception ->

                Log.e(
                    "COMPLAINT_REPOSITORY",
                    "Failed to fetch complaint",
                    exception
                )

                onResult(null)
            }
    }
}