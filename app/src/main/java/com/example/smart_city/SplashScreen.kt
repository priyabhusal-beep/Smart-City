package com.example.smart_city
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smart_city.ui.theme.SmartCityTheme
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class Splashscreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartCityTheme {
                SplashScreenContent()
                LaunchedEffect (Unit) {
                    delay(3000)
                    val intent = Intent(this@Splashscreen, GetStartedScreen::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}

@Composable
fun SplashScreenContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E4E9D),
                        Color(0xFF153672)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(100.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color.White.copy(alpha = 0.2f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.smartcity),
                    contentDescription = "Smart City Logo",
                    modifier = Modifier.size(100.dp),
                    tint = Color.Unspecified
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "SmartCity",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "THE FUTURE OF LIVING",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 2.sp
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                progress = {0.4f},
                modifier = Modifier
                    .width(180.dp)
                    .height(2.dp),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.3f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Connecting to secure network...",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(Color(0xFF4CAF50), shape = androidx.compose.foundation.shape.CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ENCRYPTED PROTOCOL ACTIVE",
                    color = Color(0xFF4CAF50),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {

    SmartCityTheme {
        SplashScreenContent()
    }
}
