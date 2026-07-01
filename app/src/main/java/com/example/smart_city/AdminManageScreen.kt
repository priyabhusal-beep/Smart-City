package com.example.smart_city

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.smart_city.model.ReportModel
import com.example.smart_city.viewmodel.ComplaintsViewModel

class AdminManageScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ManageComplaintsScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageComplaintsScreen(
    viewModel: ComplaintsViewModel = viewModel()
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }

    // Dialog state for updating status
    var showStatusDialog by remember { mutableStateOf(false) }
    var selectedComplaint by remember { mutableStateOf<ReportModel?>(null) }
    val statusOptions = listOf("Pending", "Processing", "Resolved") // CHANGED: "Completed" -> "Resolved"

    // State for showing complaint details
    var showDetailDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchAllComplaints()
    }

    val displayComplaints = if (searchQuery.isEmpty()) {
        viewModel.complaints
    } else {
        viewModel.searchComplaints(searchQuery)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SmartCity Admin", color = Color(0xFF1E3A8A), fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                actions = {
                    IconButton(onClick = { viewModel.fetchAllComplaints() }) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh", tint = Color(0xFF1E3A8A))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = { AdminBottomBarNav() },
        containerColor = Color(0xFFFBFBFE)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
        ) {
            item {
                Column {
                    Text(text = "Manage Complaints", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E3A8A))
                    Text(text = "Update status and review citizen reports", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 20.dp))
                }
            }

            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search by ticket ID or keyword...", color = Color.Gray, fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFE2E8F0),
                        focusedBorderColor = Color(0xFF1E3A8A),
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    singleLine = true
                )
            }

            if (viewModel.isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF1E3A8A))
                    }
                }
            } else if (displayComplaints.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                        Text("No complaints found", color = Color.Gray)
                    }
                }
            } else {
                items(displayComplaints) { complaint ->
                    ComplaintItemRow(
                        complaint = complaint,
                        onDescriptionClick = {
                            selectedComplaint = complaint
                            showDetailDialog = true
                        },
                        onStatusClick = {
                            selectedComplaint = complaint
                            showStatusDialog = true
                        }
                    )
                }
            }
        }

        // --- STATUS UPDATE DIALOG ---
        if (showStatusDialog && selectedComplaint != null) {
            AlertDialog(
                onDismissRequest = { showStatusDialog = false },
                title = { Text("Update Status", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        statusOptions.forEach { status ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.updateStatus(selectedComplaint!!.id, status) { success ->
                                            if (success) Toast.makeText(context, "Status updated to $status", Toast.LENGTH_SHORT).show()
                                            showStatusDialog = false
                                        }
                                    }
                                    .padding(vertical = 12.dp, horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(selected = selectedComplaint?.status?.lowercase() == status.lowercase(), onClick = null)
                                Text(text = status, modifier = Modifier.padding(start = 12.dp))
                            }
                        }
                    }
                },
                confirmButton = {},
                dismissButton = { TextButton(onClick = { showStatusDialog = false }) { Text("Cancel") } }
            )
        }

        // --- DETAIL VIEW DIALOG ---
        if (showDetailDialog && selectedComplaint != null) {
            AlertDialog(
                onDismissRequest = { showDetailDialog = false },
                title = { Text(selectedComplaint!!.issueType, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(selectedComplaint!!.imageUrl.ifEmpty { null })
                                .build(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.smartcity),
                            error = painterResource(id = R.drawable.smartcity)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Area: ${selectedComplaint!!.area}", fontWeight = FontWeight.SemiBold)
                        Text("Ward: ${selectedComplaint!!.ward}")
                        Text("Description: ${selectedComplaint!!.description}", color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Status: ${selectedComplaint!!.status.uppercase()}", color = Color(0xFF1E3A8A), fontWeight = FontWeight.Bold)
                    }
                },
                confirmButton = { Button(onClick = { showDetailDialog = false }) { Text("Close") } }
            )
        }
    }
}

@Composable
fun ComplaintItemRow(
    complaint: ReportModel,
    onDescriptionClick: () -> Unit,
    onStatusClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = complaint.imageUrl.ifEmpty { null },
                contentDescription = "Complaint Image",
                placeholder = painterResource(id = R.drawable.smartcity),
                error = painterResource(id = R.drawable.smartcity),
                modifier = Modifier.size(70.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onDescriptionClick() }
            ) {
                Text(
                    text = if (complaint.issueType.length > 15) complaint.issueType.take(15) + "..." else complaint.issueType,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF1E293B)
                )
                Text(text = "Area: ${complaint.area}", fontSize = 12.sp, color = Color.Gray)
                Text(
                    text = complaint.description,
                    fontSize = 11.sp,
                    color = Color.LightGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            BadgeStatusView(status = complaint.status, onClick = onStatusClick)

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Composable
fun BadgeStatusView(status: String, onClick: () -> Unit) {
    val (bgColor, textColor) = when (status.lowercase()) {
        "pending" -> Color(0xFFFFE4E6) to Color(0xFFEF4444)
        "processing" -> Color(0xFFEBF2FF) to Color(0xFF3B82F6)
        "completed", "resolved" -> Color(0xFFDCFCE7) to Color(0xFF22C55E)
        else -> Color.LightGray to Color.DarkGray
    }
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = bgColor
    ) {
        Text(
            text = status.uppercase(),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            fontSize = 8.sp,
            fontWeight = FontWeight.ExtraBold,
            color = textColor
        )
    }
}

@Composable
fun AdminBottomBarNav() {
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.baseline_home_24), "Home") },
            label = { Text("HOME", fontSize = 10.sp) },
            selected = false,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray)
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.ManageAccounts, "Manage") },
            label = { Text("Manage", fontSize = 10.sp) },
            selected = true,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1A237E),
                selectedTextColor = Color(0xFF1A237E),
                indicatorColor = Color(0xFFEBF2FF)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.BarChart, "Analytics") },
            label = { Text("Analytics", fontSize = 10.sp) },
            selected = false,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray)
        )
    }
}