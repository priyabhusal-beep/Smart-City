package com.example.smart_city.repo

import com.example.smart_city.model.ReportModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
class ComplaintsRepository {
    private val database = FirebaseDatabase.getInstance().getReference("Complaints")

//    fun getAllComplaints(onResult: (List<ReportModel>) -> Unit) {
//        database.get().addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val complaints = mutableListOf<ReportModel>()
//                for (snapshot in task.result.children) {
//                    val complaint = snapshot.getValue(ReportModel::class.java)
//                    if (complaint != null) complaints.add(complaint)
//                }
//                onResult(complaints.sortedByDescending { it.timestamp })
//            } else {
//                onResult(emptyList())
//            }
//        }
//    }


    fun getAllComplaints(onResult: (List<ReportModel>) -> Unit) {

        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val complaints = mutableListOf<ReportModel>()

                for (child in snapshot.children) {
                    val complaint = child.getValue(ReportModel::class.java)
                    if (complaint != null) {
                        complaints.add(complaint)
                    }
                }

                onResult(complaints.sortedByDescending { it.timestamp })
            }

            override fun onCancelled(error: DatabaseError) {
                onResult(emptyList())
            }
        })
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
                onResult(

                    complaints.sortedWith(

                        compareByDescending<ReportModel> { it.voteCount }
                            .thenByDescending { it.timestamp }

                    )

                )
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
    fun toggleVote(
        complaintId: String,
        userId: String,
        onComplete:() -> Unit
    ) {
        val complaintRef = database.child(complaintId)

        complaintRef.runTransaction(object : Transaction.Handler {

            override fun doTransaction(currentData: MutableData): Transaction.Result {

                val complaint = currentData.getValue(ReportModel::class.java)
                    ?: return Transaction.success(currentData)

                val votes = complaint.votes.toMutableMap()
                var voteCount = complaint.voteCount

                if (votes.containsKey(userId)) {
                    votes.remove(userId)
                    voteCount--
                } else {
                    votes[userId] = true
                    voteCount++
                }

                currentData.child("votes").value = votes
                currentData.child("voteCount").value = voteCount

                return Transaction.success(currentData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
                onComplete()
            }
        })
    }

    fun getComplaintsByWard(wardNo: Int, onResult: (List<ReportModel>) -> Unit) {
        database.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val complaints = mutableListOf<ReportModel>()
                for (snapshot in task.result.children) {
                    val complaint = snapshot.getValue(ReportModel::class.java)
                    if (complaint != null && complaint.ward == wardNo.toString()) {
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
