package com.example.smart_city.repo

import com.example.smart_city.model.TrafficModel
import com.google.firebase.database.FirebaseDatabase

class TrafficRepository {



        private val database =
            FirebaseDatabase.getInstance()
                .getReference("TrafficJam")

        fun getTrafficData(
            onResult: (List<TrafficModel>) -> Unit
        ) {

            database.get().addOnSuccessListener { snapshot ->


                val list =
                    mutableListOf<TrafficModel>()

                snapshot.children.forEach { snap ->
                    val traffic=
                        snap.getValue(TrafficModel::class.java)

                    if (traffic != null) {
                        android.util.Log.d(
                            "TRAFFIC_FIREBASE",
                            traffic.locationName
                        )


                        list.add(traffic)
                    }
                }

                onResult(list)
            }
        }
    }
