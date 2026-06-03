package com.example.smart_city

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style


@Composable
fun MapScreen() {
    if (LocalInspectionMode.current) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Map View Placeholder", color = Color.Gray)
        }
        return
    }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    // Remember the MapView so it's not recreated on every recomposition
    val mapView = remember {
        MapView(context).apply {
            onCreate(null)
        }
    }

    // Handle Lifecycle events
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            // Properly destroy the MapView when it leaves the composition
            // to avoid leaking GMS/gRPC resources.
            mapView.onStop()
            mapView.onDestroy()
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            mapView.apply {
                getMapAsync { map ->
                    val styleUrl =
                        "https://map-init.gallimap.com/styles/light/style.json?accessToken=6f2ce10e-f8e7-4008-8a03-384ff6f87d26"
                    map.setStyle(Style.Builder().fromUri(styleUrl))
                    {
                        style ->
                        Log.d("MAP","Style loaded successfully")

                        map.cameraPosition =
                            org.maplibre.android.camera.CameraPosition.Builder()
                                .target(
                                    org.maplibre.android.geometry.LatLng(
                                        27.7172,
                                        85.3240
                                    )
                                )
                                .zoom(12.0)
                                .build()
                    }


                }
            }
        }
    )
}
