package com.example.smart_city.util

import android.content.Context
import android.location.Geocoder
import java.util.Locale

object GeoCoderHelper {

    fun getAddress(context: Context, lat: Double, lng: Double): String {
        return try {



        val geocoder = Geocoder(context, Locale.getDefault())

        val list = geocoder.getFromLocation(lat, lng, 1)

         list?.firstOrNull()?.getAddressLine(0) ?: "Unknown"
    } catch (e: Exception){
    "Unknown Location "}
    }
}