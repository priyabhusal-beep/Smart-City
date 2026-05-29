package com.example.smart_city

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import com.example.smart_city.ui.theme.SmartCityTheme
import com.google.android.gms.location.LocationServices
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView

class MapComposable(modifier: Modifier) : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

        }
    }
}

@Composable
fun MapScreen() {
    val context = LocalContext.current

    // Added a check for LocalInspectionMode to prevent the Mapbox SDK from crashing during Android Studio Preview.
    // Mapbox MapView requires a full Android environment which is not completely available in the Layoutlib renderer.
    if (LocalInspectionMode.current) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Map Preview")
        }
        return
    }

    AndroidView(

        modifier = Modifier.fillMaxSize(),

        factory = {

            val mapView = MapView(
                context,
                MapInitOptions(context)
            )

            mapView.getMapboxMap().loadStyleUri(
                "https://tiles.gallimap.com/styles/default/style.json?api_key=${context.getString(R.string.mapbox_access_token)}"
            )

            val fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(context)

            if (
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                fusedLocationClient.lastLocation.addOnSuccessListener { location ->

                    if (location != null) {

                        val userPoint = Point.fromLngLat(
                            location.longitude,
                            location.latitude
                        )

                        mapView.getMapboxMap().setCamera(
                            CameraOptions.Builder()
                                .center(userPoint)
                                .zoom(15.0)
                                .build()
                        )
                    }
                }
            }

            mapView
        }
    )
}

fun getMapboxMap() {

}



@Preview(showBackground = true)
@Composable
fun MapPreview() {
    SmartCityTheme {
        MapScreen()
    }
}
