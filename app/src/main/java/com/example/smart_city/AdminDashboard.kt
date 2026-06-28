package com.example.smart_city

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.smart_city.model.ReportModel
import com.example.smart_city.ui.theme.SmartCityTheme
import com.example.smart_city.viewmodel.ComplaintsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdminDashboard : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)

        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())

        val wardNo = intent.getIntExtra("wardNo", 0)

        setContent {
            SmartCityTheme {
                AdminDashboardScreen(wardNo = wardNo)
            }
        }
    }
}

@Composable
fun AdminDashboardScreen(
    wardNo: Int,
    viewModel: ComplaintsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val complaints = viewModel.complaints
    val isLoading = viewModel.isLoading

    LaunchedEffect(wardNo) {
        viewModel.fetchComplaintsByWard(wardNo)
    }

    val total = complaints.size
    val pending = complaints.count { it.status.equals("Pending", ignoreCase = true) }
    val inProgress = complaints.count { it.status.equals("In Progress", ignoreCase = true) }
    val resolved = complaints.count { it.status.equals("Resolved", ignoreCase = true) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF8F9FA),
        bottomBar = { CustomBottomNavigation() }
    ) { innerPadding ->
        DashboardContent(
            contentPadding = innerPadding,
            wardNo = wardNo,
            complaints = complaints,
            isLoading = isLoading,
            total = total,
            pending = pending,
            inProgress = inProgress,
            resolved = resolved
        )
    }
}

@Composable
fun DashboardContent(
    contentPadding: PaddingValues,
    wardNo: Int,
    complaints: List<ReportModel>,
    isLoading: Boolean,
    total: Int,
    pending: Int,
    inProgress: Int,
    resolved: Int
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(
            top = 24.dp,
            bottom = contentPadding.calculateBottomPadding() + 24.dp
        )
    ) {
        item {
            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                Text(
                    text = "Admin Dashboard",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D236D)
                )

                Text(
                    text = "Ward $wardNo infrastructure oversight",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A8A)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Total Complaints",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )

                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White)
                    } else {
                        Text(
                            text = total.toString(),
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatBox(
                    value = pending.toString(),
                    label = "Pending",
                    icon = Icons.Default.Info,
                    modifier = Modifier.weight(1f),
                    iconColor = Color(0xFFFF8A65)
                )

                StatBox(
                    value = inProgress.toString(),
                    label = "In Progress",
                    icon = Icons.Default.Build,
                    modifier = Modifier.weight(1f),
                    iconColor = Color(0xFF64B5F6)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Resolved", color = Color.Gray, fontSize = 12.sp)

                        Text(
                            text = resolved.toString(),
                            color = Color(0xFF2E7D32),
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFE8F5E9),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text(
                text = "Ward $wardNo Complaints",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D236D),
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        if (isLoading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF1E3A8A))
                }
            }
        } else if (complaints.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "No complaints found for Ward $wardNo",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(24.dp)
                    )
                }
            }
        } else {
            items(complaints.take(5)) { complaint ->
                AdminComplaintCard(complaint)
            }
        }
    }
}

@Composable
fun AdminComplaintCard(complaint: ReportModel) {
    val locale = LocalConfiguration.current.locales[0]
    val formattedDate = remember(complaint.timestamp, locale) {
        SimpleDateFormat("dd/MM/yyyy HH:mm", locale).format(Date(complaint.timestamp))
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${complaint.issueType} - Ward ${complaint.ward}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF1A1A1A)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = complaint.category,
                color = Color(0xFF1E3A8A),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Area: ${complaint.area}",
                color = Color.Gray,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Status: ${complaint.status}",
                color = Color.Gray,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = complaint.description,
                color = Color.DarkGray,
                fontSize = 13.sp,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = formattedDate,
                color = Color.LightGray,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun StatBox(
    value: String,
    label: String,
    icon: ImageVector,
    modifier: Modifier,
    iconColor: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = label,
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun CustomBottomNavigation() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        color = Color.White,
        shadowElevation = 24.dp
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            NavigationBarItem(
                selected = true,
                onClick = {},
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                },
                label = {
                    Text("Admin Home", fontWeight = FontWeight.Bold)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF0D236D),
                    selectedTextColor = Color(0xFF0D236D),
                    indicatorColor = Color.Transparent
                )
            )

            NavigationBarItem(
                selected = false,
                onClick = {},
                icon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text("Manage") },
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = Color.LightGray
                )
            )

            NavigationBarItem(
                selected = false,
                onClick = {},
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text("Analytics") },
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = Color.LightGray
                )
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun FullPreview() {
    SmartCityTheme {
        AdminDashboardScreen(wardNo = 1)
    }
}