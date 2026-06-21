package com.example.smart_city

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smart_city.model.ReportModel
import com.example.smart_city.ui.theme.SmartCityTheme
import com.example.smart_city.viewmodel.ReportViewModel

class AllUserComplain : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartCityTheme {
                ComplainActivity()
            }
        }
    }
}

@Composable
fun ComplainActivity(
    viewModel: ReportViewModel = viewModel(),
    backgroundColor: Color = Color.White,
    cardBackgroundColor: Color = Color(0xFFF5F5F5),
    textColor: Color = Color.Black,
    secondaryTextColor: Color = Color.Gray
) {
    // Fetch only this user's complaints when the screen opens
    LaunchedEffect(Unit) {
        viewModel.fetchUserComplaints()
    }

    val userComplaints = viewModel.userComplaints
    val isLoading = viewModel.isLoading

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

            item {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "SmartCity",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A237E)
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Search bar removed as per request
            // Stats row (Total, Pending, Resolved) removed as per request

            item {
                Text(
                    text = "My Complaints",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF1A237E))
                    }
                }
            } else if (userComplaints.isEmpty()) {
                item {
                    Text(
                        text = "You haven't submitted any complaints yet.",
                        color = secondaryTextColor,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                }
            } else {
                items(userComplaints) { complaint ->
                    ComplaintCardItem(
                        complaint = complaint,
                        cardBackgroundColor = cardBackgroundColor,
                        textColor = textColor,
                        secondaryTextColor = secondaryTextColor
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ComplaintCardItem(
    complaint: ReportModel,
    cardBackgroundColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth().height(110.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = complaint.issueType,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Text(
                    text = complaint.area,
                    fontSize = 12.sp,
                    color = secondaryTextColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "ID: ${complaint.id.takeLast(6)}",
                    fontSize = 11.sp,
                    color = secondaryTextColor
                )
            }

            // Status badge (Matching your logic)
            val status = complaint.status.replaceFirstChar { it.uppercase() }
            val badgeColor = when (status.lowercase()) {
                "pending" -> Color(0xFFFFB74D)
                "in progress" -> Color(0xFF1E88E5)
                "resolved" -> Color(0xFF4CAF50)
                else -> Color(0xFFFF6B6B)
            }

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = badgeColor.copy(alpha = 0.15f))
            ) {
                Text(
                    text = status,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = badgeColor
                )
            }
        }
    }
}
