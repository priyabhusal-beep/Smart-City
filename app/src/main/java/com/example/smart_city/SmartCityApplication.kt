package com.example.smart_city

import android.app.Application
import com.cloudinary.android.MediaManager
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer

class SmartCityApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        MapLibre.getInstance(
            this,
            null,
            WellKnownTileServer.MapLibre
        )

        try {
            val config = hashMapOf<String, Any>(
                "cloud_name" to "dyanvmmkj"
            )
            MediaManager.init(this, config)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}