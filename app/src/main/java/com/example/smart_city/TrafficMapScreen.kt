package com.example.smart_city

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

import com.example.smart_city.model.TrafficModel
import com.example.smart_city.ui.theme.SmartCityTheme
import com.example.smart_city.viewmodel.TrafficViewModel
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import kotlin.collections.forEach
import kotlin.text.lowercase

class TrafficMapActivity : ComponentActivity() {
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartCityTheme {
                val trafficViewModel = TrafficViewModel()

                LaunchedEffect(Unit) {
                    trafficViewModel.fetchTraffic()
                }

                TrafficMapScreen(
                    trafficList = trafficViewModel.trafficList
                )
            }

            }
        }
    }


@Composable
fun TrafficMapScreen(
    trafficList: List<TrafficModel> = emptyList()
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

                    trafficList.forEach { traffic ->
                            android.util.Log.d(
                                "TRAFFIC_MARKER",
                                "${traffic.locationName} ${traffic.latitude} ${traffic.longitude}"
                            )


                        val hour =
                            java.util.Calendar.getInstance()
                                .get(java.util.Calendar.HOUR_OF_DAY)

                        val currentJamLevel = when {

                            hour in 6..10 ->
                                traffic.morningLevel

                            hour in 11..16 ->
                                traffic.afternoonLevel

                            hour in 17..20 ->
                                traffic.eveningLevel

                            else ->
                                traffic.nightLevel
                        }

                        val iconRes = when (currentJamLevel) {

                            "High" ->
                                R.drawable.baseline_circlehightraffic_24

                            "Medium" ->
                                R.drawable.baseline_circlemediumtraffic_24

                            else ->
                                R.drawable.baseline_circlelowtraffic_24
                        }

                        val drawable =
                            ContextCompat.getDrawable(context, iconRes)

                        drawable?.let {

                            val bitmap = Bitmap.createBitmap(
                                it.intrinsicWidth,
                                it.intrinsicHeight,
                                Bitmap.Config.ARGB_8888
                            )

                            val canvas = Canvas(bitmap)

                            it.setBounds(
                                0,
                                0,
                                canvas.width,
                                canvas.height
                            )

                            it.draw(canvas)

                            val icon =
                                org.maplibre.android.annotations.IconFactory
                                    .getInstance(context)
                                    .fromBitmap(bitmap)

                            map.addMarker(
                                org.maplibre.android.annotations.MarkerOptions()
                                    .position(
                                        LatLng(
                                            traffic.latitude,
                                            traffic.longitude
                                        )
                                    )
                                    .title(traffic.locationName)
                                    .snippet(currentJamLevel)
                                    .icon(icon)
                            )

                        }
                    }
                    val firstTraffic =
                        trafficList.firstOrNull()

                    if (firstTraffic != null) {

                        map.cameraPosition =
                            CameraPosition.Builder()
                                .target(
                                    LatLng(
                                        firstTraffic.latitude,
                                        firstTraffic.longitude
                                    )
                                )
                                .zoom(14.0)
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





@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SmartCityTheme {
        TrafficMapScreen()
    }
}
