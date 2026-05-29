package com.example.smart_city

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.smart_city.ui.theme.SmartCityTheme

class FullMapScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        }
    }
@Composable
fun FullMapscreen() {
    MapComposable(
        modifier = Modifier.fillMaxSize(),
    )

}


@Preview(showBackground = true)
@Composable
fun FullMapScreenPreview() {
    SmartCityTheme {
        FullMapScreen()
    }
}
