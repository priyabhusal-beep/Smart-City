package com.example.smart_city

import android.graphics.Bitmap
import android.graphics.Canvas
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.smart_city.model.ReportModel
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style


@Composable
fun MapScreen(
    complaints: List<ReportModel> = emptyList()
) {
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
        factory = { mapView },

        update = {

            mapView.getMapAsync { map ->

                val styleUrl =
                    "https://map-init.gallimap.com/styles/light/style.json?accessToken=6f2ce10e-f8e7-4008-8a03-384ff6f87d26"

                map.setStyle(
                    Style.Builder().fromUri(styleUrl)
                ) {

                    map.clear()

                    complaints.forEach { complaint ->

                        if (
                            complaint.latitude != 0.0 &&
                            complaint.longitude != 0.0
                        ) {
                            val iconRes = when (complaint.category.lowercase()) {

                                "road" -> R.drawable.baseline_location_on_24

                                "garbage" -> R.drawable.baseline_locationgarbage_on_24

                                "traffic" -> R.drawable.baseline_locationtraffic_on_24

                                else -> R.drawable.baseline_locationothers_on_24
                            }

                            // FIX: Convert Vector Drawable to Bitmap because MapLibre IconFactory cannot handle XML directly
                            val drawable = ContextCompat.getDrawable(context, iconRes)
                            drawable?.let {
                                val scale = 1.5f
                                val bitmap = Bitmap.createBitmap(
                                    (it.intrinsicWidth * scale).toInt(),
                                    (it.intrinsicHeight *scale).toInt(),
                                    Bitmap.Config.ARGB_8888
                                )
                                val canvas = Canvas(bitmap)
                                it.setBounds(0, 0, canvas.width, canvas.height)
                                it.draw(canvas)

                                val icon = org.maplibre.android.annotations.IconFactory
                                    .getInstance(context)
                                    .fromBitmap(bitmap)

                                var lat = complaint.latitude
                                var lng = complaint.longitude

                                when (complaint.category.lowercase()) {

                                    "road" -> {
                                        lat += 0.00008
                                    }

                                    "traffic" -> {
                                        lng += 0.00008
                                    }

                                    "garbage" -> {
                                        lat -= 0.00008
                                    }

                                    else -> {
                                        lng -= 0.00008
                                    }
                                }
                                map.addMarker(
                                    org.maplibre.android.annotations.MarkerOptions()
                                        .position(
                                            LatLng(
                                                lat,
                                                lng
                                            )
                                        )
                                        .title(complaint.issueType)
                                        .snippet(complaint.category)
                                        .icon(icon)
                                )
                            }
                        }
                    }

                    val firstComplaint =
                        complaints.firstOrNull {
                            it.latitude != 0.0 &&
                                    it.longitude != 0.0
                        }

                    if (firstComplaint != null) {

                        map.cameraPosition =
                            CameraPosition.Builder()
                                .target(
                                    LatLng(
                                        firstComplaint.latitude,
                                        firstComplaint.longitude
                                    )
                                )
                                .zoom(15.0)
                                .build()

                    } else {

                        map.cameraPosition =
                            CameraPosition.Builder()
                                .target(
                                    LatLng(
                                        27.7172,
                                        85.3240
                                    )
                                )
                                .zoom(11.0)
                                .build()
                    }
                }
            }
        }
    )

}
