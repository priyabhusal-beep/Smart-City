package com.example.smart_city

import android.app.Application
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
    }
}
