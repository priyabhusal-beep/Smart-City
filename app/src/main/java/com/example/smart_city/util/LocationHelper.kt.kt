package com.example.smart_city.util

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

object LocationHelper {

    @SuppressLint("MissingPermission")
    fun getLocation(
        context: Context,
        callback: (Double, Double) -> Unit,
        onFailure: () -> Unit
    ) {
        val client = LocationServices.getFusedLocationProviderClient(context)

        client.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                callback(location.latitude, location.longitude)
            } else {
                // If lastLocation is null, try to get current location
                client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
                    .addOnSuccessListener { currLocation ->
                        if (currLocation != null) {
                            callback(currLocation.latitude, currLocation.longitude)
                        } else {
                            onFailure()
                        }
                    }
                    .addOnFailureListener {e ->

                        android.util.Log.e(
                            "LOCATION_DEBUG",
                            e.message ?: "Unknown Error"
                        )

                        onFailure()

                    }
            }
        }.addOnFailureListener {e ->

            android.util.Log.e(
                "LOCATION_DEBUG",
                e.message ?: "Unknown Error"
            )

            onFailure()


        }
    }
}
