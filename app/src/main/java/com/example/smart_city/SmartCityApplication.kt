package com.example.smart_city

import android.app.Application
import com.cloudinary.android.MediaManager
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer

class SmartCityApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize MapLibre
        MapLibre.getInstance(
            this,
            null,
            WellKnownTileServer.MapLibre
        )

        // Initialize Cloudinary MediaManager
        // Note: Ensure your cloudinary credentials are in AndroidManifest.xml 
        // or passed here in a configuration map.
        try {
            MediaManager.init(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
