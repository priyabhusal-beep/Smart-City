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
import com.example.smart_city.model.ReportModel
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style


@Composable
fun MapScreen(
    complaints: List<ReportModel>
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

//    AndroidView(
//        modifier = Modifier.fillMaxSize(),
//        factory = {
//            mapView.apply {
//                getMapAsync { map ->
//                    val styleUrl =
//                        "https://map-init.gallimap.com/styles/light/style.json?accessToken=6f2ce10e-f8e7-4008-8a03-384ff6f87d26"
//                    map.setStyle(Style.Builder().fromUri(styleUrl))
//                    {
//                        style ->
//                        Log.d(
//                            "MAP_DATA",
//                            "Complaints received = ${complaints.size}"
//                        )
//                        complaints.forEach { complaint ->
//                            Log.d(
//                                "MAP_MARKER",
//                                "${complaint.issueType}:Lat=${complaint.latitude}, Lng=${complaint.longitude}"
//                            )
//
//                            if (
//                                complaint.latitude != 0.0 &&
//                                complaint.longitude != 0.0
//                            ) {
//
//                                val marker = org.maplibre.android.annotations.MarkerOptions()
//                                    .position(
//                                        org.maplibre.android.geometry.LatLng(
//                                            complaint.latitude,
//                                            complaint.longitude
//                                        )
//                                    )
//                                    .title(complaint.issueType)
//
//                                map.addMarker(marker)
//                            }
//                        }
//                        Log.d(
//                            "MAP_DATA",
//                            "Complaints received = ${complaints.size}"
//                        )
//
//                        if (complaints.isNotEmpty()) {
//
////                            map.cameraPosition =
////                                org.maplibre.android.camera.CameraPosition.Builder()
////                                    .target(
////                                        org.maplibre.android.geometry.LatLng(
////                                            complaints[0].latitude,
////                                            complaints[0].longitude
////                                        )
////                                    )
////                                    .zoom(15.0)
////                                    .build()
//                            map.cameraPosition =
//                                CameraPosition.Builder()
//                                    .target(
//                                        LatLng(
//                                            27.7172,
//                                            85.3240
//                                        )
//                                    )
//                                    .zoom(11.0)
//                                    .build()
//
//                            complaints.forEach { complaint ->
//                                // add markers
//                            }
//                        }
//                    }
//
//
//                }
//            }
//        }
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

                            val icon = org.maplibre.android.annotations.IconFactory
                                .getInstance(context)
                                .fromResource(iconRes)

                            map.addMarker(
                                org.maplibre.android.annotations.MarkerOptions()
                                    .position(
                                        LatLng(
                                            complaint.latitude,
                                            complaint.longitude
                                        )
                                    )
                                    .title(complaint.issueType)
                                    .snippet(complaint.category)
                                    .icon(icon)
                            )
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
