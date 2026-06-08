package com.example.smart_city

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smart_city.ui.theme.SmartCityTheme

class AllUserComplain : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartCityTheme {
                // Default dark mode off for standalone preview
                ComplainActivity(
                    isDarkMode = false,
                    backgroundColor = Color.White,
                    cardBackgroundColor = Color(0xFFF5F5F5),
                    textColor = Color.Black,
                    secondaryTextColor = Color.Gray
                )
            }
        }
    }
}

@Composable
fun ComplainActivity(
    isDarkMode: Boolean = false,
    backgroundColor: Color = Color.White,
    cardBackgroundColor: Color = Color(0xFFF5F5F5),
    textColor: Color = Color.Black,
    secondaryTextColor: Color = Color.Gray
) {
    // Sample complaints data
    val complaints = listOf(
        Pair("Road pothole near market", "Open"),
        Pair("Street light broken", "In Progress"),
        Pair("Water supply issue", "Resolved"),
        Pair("Garbage not collected", "Open"),
        Pair("Broken sidewalk", "Pending")
    )

    Scaffold(
        containerColor = backgroundColor
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(backgroundColor)
                .padding(horizontal = 20.dp)
        ) {

            // ── Section: Header ──────────────────────────────────────
            item {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "SmartCity",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A237E)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Section: Search / Filter Bar
            item {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search complaints...", color = secondaryTextColor) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1A237E),
                        unfocusedBorderColor = secondaryTextColor,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Section: Summary / Stats Cards
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Stats Card 1
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "5",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A237E)
                            )
                            Text(
                                "Total",
                                fontSize = 12.sp,
                                color = secondaryTextColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Stats Card 2
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "3",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E88E5)
                            )
                            Text(
                                "Pending",
                                fontSize = 12.sp,
                                color = secondaryTextColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Stats Card 3
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "1",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4CAF50)
                            )
                            Text(
                                "Resolved",
                                fontSize = 12.sp,
                                color = secondaryTextColor
                            )
                        }
                    }
                }
            }

            // ── Section: Complaints List Title
            item {
                Text(
                    text = "All Complaints",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // ── Dynamic List of Complaints
            items(complaints.size) { index ->
                ComplaintCardItem(
                    title = complaints[index].first,
                    status = complaints[index].second,
                    isDarkMode = isDarkMode,
                    cardBackgroundColor = cardBackgroundColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // ── Bottom Spacing
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ComplaintCardItem(
    title: String,
    status: String,
    isDarkMode: Boolean,
    cardBackgroundColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Status: $status",
                    fontSize = 13.sp,
                    color = secondaryTextColor
                )
            }

            // Status badge
            Card(
                modifier = Modifier
                    .height(40.dp)
                    .width(80.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when (status) {
                        "Open" -> Color(0xFFFF6B6B).copy(alpha = 0.2f)
                        "In Progress" -> Color(0xFF1E88E5).copy(alpha = 0.2f)
                        "Resolved" -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                        else -> Color(0xFFFFB74D).copy(alpha = 0.2f)
                    }
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = status,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (status) {
                            "Open" -> Color(0xFFFF6B6B)
                            "In Progress" -> Color(0xFF1E88E5)
                            "Resolved" -> Color(0xFF4CAF50)
                            else -> Color(0xFFFFB74D)
                        }
                    )
                }
            }
        }
    }
}

// ─── Preview
@Preview(showBackground = true)
@Composable
fun AllComplaintPreview() {
    SmartCityTheme {
        ComplainActivity()
    }
}