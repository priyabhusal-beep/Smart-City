package com.example.smart_city

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smart_city.model.ReportModel
import com.example.smart_city.ui.theme.SmartCityTheme

class FullMapScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartCityTheme {
                FullMapscreen(
                    complaints = emptyList()
                )
            }
        }
    }
}

@Composable
fun FullMapscreen(

    complaints: List<ReportModel>
) {

    Log.d(
        "FULL_MAP",
        "Complaints size = ${complaints.size}"
    )
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            Text("🔴 Road   ")
            Text("🟢 Garbage   ")
            Text("🔵 Traffic   ")
            Text("🟡 Others")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        MapScreen(complaints)
    }
}


@Preview(showBackground = true)
@Composable
fun FullMapScreenPreview() {
    SmartCityTheme {
        FullMapscreen(complaints = emptyList())
    }
}
