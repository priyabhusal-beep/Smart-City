package com.example.smart_city

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smart_city.ui.theme.SmartCityTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smart_city.viewmodel.ComplaintsViewModel
import com.example.smart_city.model.ReportModel
import coil3.compose.AsyncImage

class ComplaintDetails : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val complaintId = intent.getStringExtra("complaintId") ?: ""

        setContent {
            SmartCityTheme {
                ComplaintDetailsActivity(
                    complaintId = complaintId
                )
            }
        }
    }
}

@Composable
fun ComplaintDetailsActivity(
    complaintId: String = "",
    viewModel: ComplaintsViewModel = viewModel()
) {
    val complaint = viewModel.selectedComplaint

    LaunchedEffect(complaintId) {
        viewModel.fetchComplaintById(complaintId)
    }

    if (complaint == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

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
            ComplaintImage(complaint = complaint)
            Spacer(modifier = Modifier.height(20.dp))
            ComplaintCard(complaint = complaint)
            Spacer(modifier = Modifier.height(18.dp))

            LocationSection(complaint = complaint)

            Spacer(modifier = Modifier.height(10.dp))

            MapSection(complaint = complaint)
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
fun ComplaintImage(complaint: ReportModel) {
    Box {
        AsyncImage(
            model = complaint.imageUrl,
            contentDescription = "Complaint Image",
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
                text = "${complaint.voteCount} Citizens Upvoted",
                color = Color.White,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun ComplaintCard(complaint: ReportModel) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Text(
                text = complaint.issueType,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0B2E83)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = complaint.category,
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                shape = RoundedCornerShape(30.dp),
                color = when (complaint.status.lowercase()) {

                    "resolved" -> Color(0xFFE7F7EC)

                    "processing",
                    "in progress" -> Color(0xFFE8F0FF)

                    else -> Color(0xFFFFF5D8)
                }
            ) {

                Text(
                    text = complaint.status,
                    modifier = Modifier.padding(
                        horizontal = 14.dp,
                        vertical = 6.dp
                    ),
                    color = when (complaint.status.lowercase()) {

                        "resolved" -> Color(0xFF1B8A3D)

                        "processing",
                        "in progress" -> Color(0xFF2962FF)

                        else -> Color(0xFFDAA520)
                    },
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )

            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = complaint.description,
                fontSize = 15.sp,
                lineHeight = 22.sp,
                color = Color.DarkGray
            )

        }

    }

}

@Composable
fun LocationSection(
    complaint: ReportModel
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Text(
                text = "Incident Location",
                color = Color(0xFF0B2E83),
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                verticalAlignment = Alignment.Top
            ) {

                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = Color(0xFF0B2E83),
                    modifier = Modifier.size(22.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {

                    Text(
                        text = complaint.area,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = " ${complaint.ward}",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )

                }

            }

        }

    }

}

@Composable
fun MapSection(complaint: ReportModel) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        MapScreen(complaints = listOf(complaint))
        Button(
            onClick = {
                context.startActivity(Intent(context, FullMapScreen::class.java))
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0B2E83))
        ) {
            Text("View Full Map")
        }
    }
}

@Composable
fun CDBottomNavigationBar() {
    val context = LocalContext.current
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = false,
            onClick = {
                val intent = Intent(context, AdminDashboard::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                context.startActivity(intent)
            },
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("HOME") }
        )
        NavigationBarItem(
            selected = true,
            onClick = {
                val intent = Intent(context, AdminManageScreen::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                context.startActivity(intent)
            },
            icon = { Icon(Icons.Default.Settings, null) },
            label = { Text("Manage") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF0B2E83),
                selectedTextColor = Color(0xFF0B2E83),
                indicatorColor = Color(0xFFEAF2FF)
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Outlined.BarChart, null) },
            label = { Text("Analytics") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ComplaintDetailsPreview() {
    SmartCityTheme {
        ComplaintDetailsActivity(complaintId = "")
    }
}
