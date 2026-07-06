package com.example.smart_city

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smart_city.model.User
import com.example.smart_city.ui.theme.SmartCityTheme
import com.example.smart_city.viewmodel.NotificationViewModel

class AdminNotificationActivity : ComponentActivity() {

    private val viewModel: NotificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SmartCityTheme {
                AdminNotificationScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminNotificationScreen(viewModel: NotificationViewModel) {
    val context = LocalContext.current

    val allUsers by viewModel.allUsers.collectAsState()
    val viewModelMessage by viewModel.message.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var selectedTitle by remember { mutableStateOf("Complaint Resolved") }
    var titleExpanded by remember { mutableStateOf(false) }

    var messageText by remember { mutableStateOf("") }

    var selectedRecipient by remember { mutableStateOf("All Users") }
    var recipientExpanded by remember { mutableStateOf(false) }

    var selectedUser by remember { mutableStateOf<User?>(null) }
    var showUserDialog by remember { mutableStateOf(false) }
    var showSendDialog by remember { mutableStateOf(false) }

    val titleOptions = listOf(
        "Complaint Resolved",
        "Complaint In Progress",
        "Complaint Pending",
        "General Announcement",
        "Emergency Alert"
    )

    val recipientOptions = listOf(
        "All Users",
        "Specific User"
    )

    LaunchedEffect(Unit) {
        viewModel.loadAllUsers()
    }

    LaunchedEffect(viewModelMessage) {
        if (viewModelMessage.isNotEmpty()) {
            Toast.makeText(context, viewModelMessage, Toast.LENGTH_SHORT).show()

            if (viewModelMessage.contains("successfully", ignoreCase = true)) {
                messageText = ""
                selectedUser = null
                selectedRecipient = "All Users"
            }

            viewModel.clearMessage()
        }
    }

    if (showUserDialog) {
        UserSearchDialog(
            users = allUsers,
            onDismiss = { showUserDialog = false },
            onUserSelected = { user ->
                selectedUser = user
                showUserDialog = false
            }
        )
    }

    if (showSendDialog) {
        ConfirmSendDialog(
            selectedTitle = selectedTitle,
            messageText = messageText,
            selectedRecipient = selectedRecipient,
            selectedUser = selectedUser,
            onCancel = { showSendDialog = false },
            onSend = {
                showSendDialog = false

                when (selectedRecipient) {
                    "All Users" -> {
                        viewModel.sendToAll(
                            title = selectedTitle,
                            message = messageText
                        )
                    }

                    "Specific User" -> {
                        selectedUser?.let { user ->
                            viewModel.sendToSpecificUser(
                                title = selectedTitle,
                                message = messageText,
                                user = user
                            )
                        }
                    }
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Send Notification",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            ExposedDropdownMenuBox(
                expanded = titleExpanded,
                onExpandedChange = { titleExpanded = !titleExpanded }
            ) {
                OutlinedTextField(
                    value = selectedTitle,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Notification Title") },
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )

                ExposedDropdownMenu(
                    expanded = titleExpanded,
                    onDismissRequest = { titleExpanded = false }
                ) {
                    titleOptions.forEach { title ->
                        DropdownMenuItem(
                            text = { Text(title) },
                            onClick = {
                                selectedTitle = title
                                titleExpanded = false
                            }
                        )
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                label = { Text("Message") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4
            )
        }

        item {
            ExposedDropdownMenuBox(
                expanded = recipientExpanded,
                onExpandedChange = { recipientExpanded = !recipientExpanded }
            ) {
                OutlinedTextField(
                    value = selectedRecipient,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Send To") },
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )

                ExposedDropdownMenu(
                    expanded = recipientExpanded,
                    onDismissRequest = { recipientExpanded = false }
                ) {
                    recipientOptions.forEach { recipient ->
                        DropdownMenuItem(
                            text = { Text(recipient) },
                            onClick = {
                                selectedRecipient = recipient
                                recipientExpanded = false
                                selectedUser = null
                            }
                        )
                    }
                }
            }
        }

        if (selectedRecipient == "Specific User") {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showUserDialog = true },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5FF))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFE1ECFF),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.padding(8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = selectedUser?.name ?: "Select User",
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = selectedUser?.email ?: "Tap to search registered users",
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        item {
            Button(
                onClick = {
                    if (messageText.isBlank()) {
                        Toast.makeText(
                            context,
                            "Please enter message",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    if (selectedRecipient == "Specific User" && selectedUser == null) {
                        Toast.makeText(
                            context,
                            "Please select a user",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    showSendDialog = true
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Send Notification")
                }
            }
        }
    }
}

@Composable
fun ConfirmSendDialog(
    selectedTitle: String,
    messageText: String,
    selectedRecipient: String,
    selectedUser: User?,
    onCancel: () -> Unit,
    onSend: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text("Confirm Notification")
        },
        text = {
            Column {
                Text("Are you sure you want to send this notification?")

                Spacer(modifier = Modifier.height(16.dp))

                Text("Title:", fontWeight = FontWeight.Bold)
                Text(selectedTitle)

                Spacer(modifier = Modifier.height(12.dp))

                Text("Recipient:", fontWeight = FontWeight.Bold)
                Text(
                    if (selectedRecipient == "All Users") {
                        "All Users"
                    } else {
                        selectedUser?.name ?: "Selected User"
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Message:", fontWeight = FontWeight.Bold)
                Text(messageText)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
        },
        confirmButton = {
            Button(onClick = onSend) {
                Text("Send")
            }
        }
    )
}

@Composable
fun UserSearchDialog(
    users: List<User>,
    onDismiss: () -> Unit,
    onUserSelected: (User) -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    val filteredUsers = remember(searchText, users) {
        val query = searchText.lowercase().trim()

        if (query.isBlank()) {
            users
        } else {
            users.filter { user ->
                user.name.lowercase().contains(query) ||
                        user.email.lowercase().contains(query) ||
                        user.phone.lowercase().contains(query)
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Select User")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text("Search name, email, or phone") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (filteredUsers.isEmpty()) {
                    Text("No user found", color = Color.Gray)
                } else {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 360.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredUsers) { user ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onUserSelected(user)
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFF7F9FC)
                                )
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = user.name.ifBlank { "No name" },
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = user.email,
                                        color = Color.Gray
                                    )

                                    if (user.phone.isNotBlank()) {
                                        Text(
                                            text = user.phone,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}