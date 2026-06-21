package com.example.smart_city

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smart_city.ui.theme.SmartCityTheme

class ComplaintDetails : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            SmartCityTheme {
                ComplaintDetailsActivity()
            }
        }
    }
}

@Composable
fun ComplaintDetailsActivity() {

    Scaffold(
        bottomBar = {
            CDBottomNavigationBar()
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            Header()

            Spacer(modifier = Modifier.height(20.dp))

            ComplaintImage()

            Spacer(modifier = Modifier.height(20.dp))

            ComplaintCard()

            Spacer(modifier = Modifier.height(20.dp))

            LocationSection()

            Spacer(modifier = Modifier.height(16.dp))

            MapSection()

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun Header() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "SmartCity",
            color = Color(0xFF0B2E83),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Icon(
            imageVector = Icons.Default.NotificationsNone,
            contentDescription = "Notifications",
            tint = Color(0xFF0B2E83)
        )
    }
}

@Composable
fun ComplaintImage() {

    Box {

        Image(
            painter = painterResource(id = R.drawable.brokenroad),
            contentDescription = "Utility Pole",
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(14.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(Color(0xFF0B2E83))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Outlined.ThumbUp,
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = "48 Citizens Upvoted",
                color = Color.White,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun ComplaintCard() {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Text(
                text = "Utility Pole Damage -\nSector 7",
                color = Color(0xFF0B2E83),
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(50.dp),
                color = Color(0xFFFFF8E1)
            ) {

                Row(
                    modifier = Modifier.padding(
                        horizontal = 14.dp,
                        vertical = 8.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFC107))
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "Pending Review",
                        color = Color(0xFFDAA520)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text =
                    "The main utility pole near the central park entrance has developed significant cracks at the base. Visible wiring exposure after the recent storm. Poses an immediate risk to pedestrian safety and power stability in the neighboring block.",

                color = Color.Gray,
                lineHeight = 26.sp
            )

        }
    }
}

@Composable
fun LocationSection() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row {

            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = "",
                tint = Color(0xFF0B2E83)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Incident\nLocation",
                color = Color(0xFF0B2E83)
            )
        }

        Text(
            text = "DOWNTOWN\nDISTRICT",
            color = Color.DarkGray,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun MapSection() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "Map Preview",
            color = Color.DarkGray
        )
    }
}

@Composable
fun CDBottomNavigationBar() {

    NavigationBar(
        containerColor = Color.White
    ) {

        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = {
                Icon(Icons.Default.Home, null)
            },
            label = {
                Text("HOME")
            }
        )

        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = {
                Icon(Icons.Default.Settings, null)
            },
            label = {
                Text("Manage")
            }
        )

        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = {
                Icon(Icons.Outlined.BarChart, null)
            },
            label = {
                Text("Analytics")
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ComplaintDetailsPreview() {

    SmartCityTheme {
        ComplaintDetailsActivity()
    }
}