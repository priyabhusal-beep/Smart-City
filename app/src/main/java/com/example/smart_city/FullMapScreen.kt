package com.example.smart_city

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.smart_city.ui.theme.SmartCityTheme

class FullMapScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartCityTheme {
                FullMapscreen()
            }
        }
    }
}

@Composable
fun FullMapscreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        MapScreen()
    }
}


@Preview(showBackground = true)
@Composable
fun FullMapScreenPreview() {
    SmartCityTheme {
        FullMapscreen()
    }
}
