package com.example.smart_city

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*

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
                ComplainActivity()
            }
        }
    }
}

@Composable
fun ComplainActivity() {

    // ─── State Variables ───────────────────────────────────────────────
    // Add your state variables here
    // Example: var searchQuery by remember { mutableStateOf("") }


    // ─── Main Layout ───────────────────────────────────────────────────
    Scaffold(
        topBar = {
            // ── Top App Bar ──────────────────────────────────────────
            // Add your TopAppBar widget here

        },
        floatingActionButton = {
            // ── FAB Button ───────────────────────────────────────────
            // Add your FloatingActionButton widget here

        },
        containerColor = Color.White
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .background(Color.White)
        ) {

            // ── Section: Header ──────────────────────────────────────
            item {
                Spacer(modifier = Modifier.height(16.dp))

                // Add your Header widget here

                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Section: Search / Filter Bar
            item {

                // Add your Search Bar widget here

                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Section: Summary / Stats Cards
            item {

                // Add your Stats/Summary widget here

                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Section: Complaints List
            item {
                Text(
                    text = "All Complaints",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // ── Dynamic List
            // Replace with: items(yourList) { item -> YourCardWidget(item) }
            item {

                // Add your Complaint Card widget here

                Spacer(modifier = Modifier.height(10.dp))
            }

            // ── Bottom Spacing
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

        }
    }
}


// ─── Child Composables / Widgets
// Add your reusable widget composables below this line
// Example:
// @Composable
// fun ComplaintCard(data: ComplaintData) { ... }


// ─── Preview

@Preview(showBackground = true)
@Composable
fun AllComplaintPreview() {
    SmartCityTheme {
        ComplainActivity()
    }
}