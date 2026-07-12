package com.example.smart_city.repo

import android.util.Log
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

            val list = mutableListOf<TrafficModel>()


            snapshot.children.forEach { snap ->


                val traffic = TrafficModel(

                    id = snap.key ?: "",

                    locationName =
                        snap.child("locationName")
                            .getValue(String::class.java) ?: "",

                    latitude =
                        snap.child("latitude")
                            .getValue(Double::class.java) ?: 0.0,

                    longitude =
                        snap.child("longitude")
                            .getValue(Double::class.java) ?: 0.0,

                    morningLevel =
                        snap.child("morningLevel")
                            .getValue(String::class.java) ?: "Low",

                    afternoonLevel =
                        snap.child("afternoonLevel")
                            .getValue(String::class.java) ?: "Low",

                    eveningLevel =
                        snap.child("eveningLevel")
                            .getValue(String::class.java) ?: "Low",

                    nightLevel =
                        snap.child("nightLevel")
                            .getValue(String::class.java) ?: "Low",

                    jamLevel =
                        snap.child("jamLevel")
                            .getValue(String::class.java) ?: ""

                )


                Log.d(
                    "TRAFFIC_FIXED",
                    "${traffic.locationName} -> ${traffic.latitude}, ${traffic.longitude}"
                )


                list.add(traffic)
            }


            onResult(list)

        }
    }
}