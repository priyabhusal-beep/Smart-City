package com.example.smart_city

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smart_city.ui.theme.blue

class Userprofile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Userprofilebody()
        }
    }
}

@Composable
fun Userprofilebody() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(20.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Smart City",
                style = TextStyle(
                    fontSize = 20.sp,
                    color = Color(0xFF1A237E),
                    fontWeight = FontWeight.Bold
                )
            )

            Icon(
                painter = painterResource(R.drawable.baseline_notifications_none_24),
                contentDescription = null,
                tint = Color(0xFF1A237E)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Profile Section
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.user),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "John Doe",
                style = TextStyle(
                    fontSize = 28.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // The "Top Contributor" badge card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = blue // Light red background
                )
            ) {
                Text(
                    text = "Top Contributor",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Color.Blue,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically


        ) {
            Column(

            ) {
                Text(
                    text = "12",
                    style = TextStyle(
                        fontSize = 30.sp,
                        color = Color.Blue,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Reports",
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.DarkGray
                    )
                )


            }
            Column() {
                Text(
                    text = "10",
                    style = TextStyle(
                        fontSize = 30.sp,
                        color = Color.Blue,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Votes",
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.DarkGray
                    )
                )


            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "8",
                    style = TextStyle(
                        fontSize = 30.sp,
                        color = Color.Green,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Resolved",
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.DarkGray
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
//        Card(
//            modifier = Modifier.width(300.dp)
//                .fillMaxWidth()
//                .height(80.dp),
//            shape = RoundedCornerShape(12.dp),
//            colors = CardDefaults.cardColors(
//                containerColor = Color.DarkGray
//            )
//        ) {
//            Row(
//                modifier = Modifier.padding(16.dp),
//                verticalAlignment = Alignment.CenterVertically
//
//            ) { }
//        }

    }
}

@Preview(showBackground = true)
@Composable
fun UserprofilePreview() {
    Userprofilebody()
}
