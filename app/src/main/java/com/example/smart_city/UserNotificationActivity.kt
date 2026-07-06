package com.example.smart_city

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smart_city.model.Notification
import com.example.smart_city.ui.theme.SmartCityTheme
import com.example.smart_city.utils.ThemePreference
import com.example.smart_city.viewmodel.NotificationViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserNotificationActivity : ComponentActivity() {

    private val viewModel: NotificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val wardNo = intent.getIntExtra("wardNo", 0)
        val isDarkMode = ThemePreference.getDarkMode(this)

        setContent {
            SmartCityTheme {
                UserNotificationScreen(
                    viewModel = viewModel,
                    wardNo = wardNo,
                    isDarkMode = isDarkMode,
                    onBack = { finish() }
                )
            }
        }
    }
}

@Composable
fun UserNotificationScreen(
    viewModel: NotificationViewModel,
    wardNo: Int,
    isDarkMode: Boolean,
    onBack: () -> Unit
) {
    val notifications by viewModel.notifications.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val backgroundColor =
        if (isDarkMode) Color(0xFF121212) else Color(0xFFF6F8FC)

    val emptyTextColor =
        if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray

    LaunchedEffect(Unit) {
        viewModel.loadUserNotifications(wardNo)
    }

    LaunchedEffect(notifications) {
        if (notifications.isNotEmpty()) {
            viewModel.markAllAsRead(wardNo)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = PrimaryBlue
                )
            }

            Text(
                text = "Notifications",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
        }

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryBlue)
                }
            }

            notifications.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No notifications yet",
                        color = emptyTextColor
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(notifications) { notification ->
                        NotificationCard(
                            notification = notification,
                            isDarkMode = isDarkMode
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationCard(
    notification: Notification,
    isDarkMode: Boolean
) {
    val date = remember(notification.createdAt) {
        SimpleDateFormat(
            "dd MMM yyyy, hh:mm a",
            Locale.getDefault()
        ).format(Date(notification.createdAt))
    }

    val cardColor =
        if (isDarkMode) Color(0xFF1E1E1E) else Color.White

    val titleColor =
        if (isDarkMode) Color.White else Color(0xFF1A1A1A)

    val messageColor =
        if (isDarkMode) Color(0xFFE0E0E0) else Color.DarkGray

    val dateColor =
        if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray

    val iconBackground =
        if (isDarkMode) Color(0xFF26324A) else Color(0xFFEAF1FF)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = CircleShape,
                color = iconBackground
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.padding(10.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.title,
                    fontWeight = FontWeight.Bold,
                    color = titleColor,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = notification.message,
                    color = messageColor,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = date,
                    color = dateColor,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}