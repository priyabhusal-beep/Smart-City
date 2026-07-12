package com.example.smart_city

import android.content.Intent
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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smart_city.model.ReportModel
import com.example.smart_city.ui.theme.SmartCityTheme
import com.example.smart_city.viewmodel.ComplaintsViewModel
import java.text.SimpleDateFormat
import java.util.Date

class AdminDashboard : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)

        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        windowInsetsController.hide(
            WindowInsetsCompat.Type.statusBars()
        )

        setContent {
            SmartCityTheme {
                AdminDashboardScreen()
            }
        }
    }
}

@Composable
fun AdminDashboardScreen(
    viewModel: ComplaintsViewModel = viewModel()
) {
    val complaints = viewModel.complaints
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage
    val wardNo = viewModel.adminWardNo

    /*
     Load all complaints because Manage Complaints also uses
     fetchAllComplaints().
    */
    LaunchedEffect(Unit) {
        viewModel.fetchAllComplaints()
    }

    /*
     Refresh the dashboard whenever the user returns from
     Manage Complaints.
    */
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->

            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.fetchAllComplaints()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    /*
     Calculate real complaint totals from Firebase data.
    */
    val totalComplaints = complaints.size

    val pendingComplaints = complaints.count { complaint ->
        normalizeComplaintStatus(complaint.status) == "pending"
    }

    val inProgressComplaints = complaints.count { complaint ->
        normalizeComplaintStatus(complaint.status) == "in_progress"
    }

    val resolvedComplaints = complaints.count { complaint ->
        normalizeComplaintStatus(complaint.status) == "resolved"
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF8F9FA),
        bottomBar = {
            CustomBottomNavigation(
                wardNo = wardNo
            )
        }
    ) { innerPadding ->

        AdminDashboardContent(
            contentPadding = innerPadding,
            wardNo = wardNo,
            complaints = complaints,
            isLoading = isLoading,
            errorMessage = errorMessage,
            total = totalComplaints,
            pending = pendingComplaints,
            inProgress = inProgressComplaints,
            resolved = resolvedComplaints
        )
    }
}

private fun normalizeComplaintStatus(
    status: String
): String {
    val normalizedStatus = status
        .trim()
        .lowercase()
        .replace("_", " ")
        .replace("-", " ")
        .replace(Regex("\\s+"), " ")

    return when (normalizedStatus) {

        "pending" -> "pending"

        "processing",
        "in progress",
        "inprogress",
        "working" -> "in_progress"

        "resolved",
        "completed",
        "complete" -> "resolved"

        else -> "unknown"
    }
}

@Composable
fun AdminDashboardContent(
    contentPadding: PaddingValues,
    wardNo: Int,
    complaints: List<ReportModel>,
    isLoading: Boolean,
    errorMessage: String,
    total: Int,
    pending: Int,
    inProgress: Int,
    resolved: Int
) {
    val context = LocalContext.current

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Admin Dashboard",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0D236D)
                    )

                    Text(
                        text = if (wardNo > 0) {
                            "Ward $wardNo infrastructure oversight"
                        } else {
                            "Real-time infrastructure oversight"
                        },
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }

                IconButton(
                    onClick = {
                        val intent = Intent(
                            context,
                            AdminNotificationActivity::class.java
                        )

                        intent.putExtra("wardNo", wardNo)
                        context.startActivity(intent)
                    }
                ) {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.baseline_notifications_24
                        ),
                        contentDescription = "Admin Notifications",
                        tint = Color(0xFF0D236D),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        /*
         Only display the error card when the message is not empty.
        */
        if (errorMessage.isNotBlank()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFEBEE)
                    )
                ) {
                    Text(
                        text = errorMessage,
                        color = Color(0xFFC62828),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E3A8A)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Total Complaints",
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 14.sp
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(36.dp),
                            color = Color.White,
                            strokeWidth = 3.dp
                        )
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

            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AdminStatBox(
                    value = if (isLoading) "..." else pending.toString(),
                    label = "Pending",
                    icon = Icons.Default.Info,
                    modifier = Modifier.weight(1f),
                    iconColor = Color(0xFFFF8A65),
                    iconBackgroundColor = Color(0xFFFFF3E0)
                )

                AdminStatBox(
                    value = if (isLoading) "..." else inProgress.toString(),
                    label = "In Progress",
                    icon = Icons.Default.Build,
                    modifier = Modifier.weight(1f),
                    iconColor = Color(0xFF1976D2),
                    iconBackgroundColor = Color(0xFFE3F2FD)
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Resolved",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )

                        Text(
                            text = if (isLoading) {
                                "..."
                            } else {
                                resolved.toString()
                            },
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
                            contentDescription = "Resolved",
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (wardNo > 0) {
                        "Ward $wardNo Complaints"
                    } else {
                        "Recent Complaints"
                    },
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D236D),
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )

                TextButton(
                    onClick = {
                        val intent = Intent(
                            context,
                            AdminManageScreen::class.java
                        )

                        intent.putExtra("wardNo", wardNo)
                        context.startActivity(intent)
                    }
                ) {
                    Text(
                        text = "View All",
                        color = Color(0xFF1E3A8A),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFF1E3A8A)
                    )
                }
            }
        } else if (complaints.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 1.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = null,
                            tint = Color.LightGray,
                            modifier = Modifier.size(40.dp)
                        )

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        Text(
                            text = "No complaints found",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        } else {
            items(
                items = complaints
                    .sortedByDescending { it.timestamp }
                    .take(5),
                key = { complaint ->
                    complaint.id.ifBlank {
                        "${complaint.userId}_${complaint.timestamp}"
                    }
                }
            ) { complaint ->

                AdminComplaintCard(
                    complaint = complaint
                )
            }
        }
    }
}

@Composable
fun AdminComplaintCard(
    complaint: ReportModel
) {
    val locale = LocalConfiguration.current.locales[0]

    val formattedDate = remember(
        complaint.timestamp,
        locale
    ) {
        if (complaint.timestamp > 0L) {
            SimpleDateFormat(
                "dd/MM/yyyy HH:mm",
                locale
            ).format(
                Date(complaint.timestamp)
            )
        } else {
            "Date unavailable"
        }
    }

    val normalizedStatus =
        normalizeComplaintStatus(complaint.status)

    val statusColor = when (normalizedStatus) {
        "pending" -> Color(0xFFEF6C00)
        "in_progress" -> Color(0xFF1976D2)
        "resolved" -> Color(0xFF2E7D32)
        else -> Color.Gray
    }

    val statusBackgroundColor = when (normalizedStatus) {
        "pending" -> Color(0xFFFFF3E0)
        "in_progress" -> Color(0xFFE3F2FD)
        "resolved" -> Color(0xFFE8F5E9)
        else -> Color(0xFFF5F5F5)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = complaint.issueType.ifBlank {
                            "Complaint"
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1A1A1A)
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = complaint.category.ifBlank {
                            "Uncategorized"
                        },
                        color = Color(0xFF1E3A8A),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = statusBackgroundColor
                ) {
                    Text(
                        text = complaint.status.ifBlank {
                            "Unknown"
                        },
                        color = statusColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 6.dp
                        )
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = if (complaint.wardNo > 0) {
                    "Ward: ${complaint.wardNo}"
                } else {
                    "Ward: ${complaint.ward}"
                },
                color = Color.DarkGray,
                fontSize = 13.sp
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Area: ${
                    complaint.area.ifBlank {
                        "Not provided"
                    }
                }",
                color = Color.Gray,
                fontSize = 13.sp
            )

            if (complaint.description.isNotBlank()) {
                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = complaint.description,
                    color = Color.DarkGray,
                    fontSize = 13.sp,
                    maxLines = 2
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = formattedDate,
                color = Color.Gray,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun AdminStatBox(
    value: String,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    iconColor: Color,
    iconBackgroundColor: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Surface(
                modifier = Modifier.size(38.dp),
                shape = RoundedCornerShape(10.dp),
                color = iconBackgroundColor
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconColor,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
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
fun CustomBottomNavigation(
    wardNo: Int
) {
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(
            topStart = 32.dp,
            topEnd = 32.dp
        ),
        color = Color.White,
        shadowElevation = 24.dp
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            modifier = Modifier.windowInsetsPadding(
                WindowInsets.navigationBars
            )
        ) {
            NavigationBarItem(
                selected = true,
                onClick = {},
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Admin Home",
                        modifier = Modifier.size(28.dp)
                    )
                },
                label = {
                    Text(
                        text = "Admin Home",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF0D236D),
                    selectedTextColor = Color(0xFF0D236D),
                    indicatorColor = Color.Transparent
                )
            )

            NavigationBarItem(
                selected = false,
                onClick = {
                    val intent = Intent(
                        context,
                        AdminManageScreen::class.java
                    )

                    intent.putExtra("wardNo", wardNo)
                    context.startActivity(intent)
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Manage",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text("Manage")
                },
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )

            NavigationBarItem(
                selected = false,
                onClick = {
                    val intent = Intent(
                        context,
                        AdminAnalyticsActivity::class.java
                    )

                    intent.putExtra("wardNo", wardNo)
                    context.startActivity(intent)
                },
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = "Analytics",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text("Analytics")
                },
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}