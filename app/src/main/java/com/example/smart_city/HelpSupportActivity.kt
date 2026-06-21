package com.example.smart_city

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smart_city.ui.theme.SmartCityTheme

data class FAQItem(val question: String, val answer: String)

class HelpSupportActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartCityTheme {
                HelpSupportScreenContent(onBackPressed = { finish() })
            }
        }
    }
}

@Composable
fun HelpSupportScreenContent(onBackPressed: () -> Unit = {}) {
    var isDarkMode by remember { mutableStateOf(false) }
    var expandedFAQ by remember { mutableStateOf<Int?>(null) }

    val backgroundColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFF8F9FA)
    val cardColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDarkMode) Color(0xFFE0E0E0) else Color.Black
    val secondaryTextColor = if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray

    val faqs = listOf(
        FAQItem(
            "How do I report an issue?",
            "Navigate to 'My Complaints' and fill in the location, category, and detailed description of the issue. Add photos if possible. Then click 'Submit'."
        ),
        FAQItem(
            "How long does it take to resolve a complaint?",
            "Most complaints are reviewed within 24-48 hours. The timeline depends on the issue category."
        ),
        FAQItem(
            "What happens after I submit a complaint?",
            "Your complaint enters 'Open' status. The city department reviews it and updates to 'In Progress' or 'Resolved'."
        ),
        FAQItem(
            "Can I edit or delete my complaint?",
            "You can edit while it's 'Open'. Once 'In Progress' or 'Resolved', it becomes read-only."
        ),
        FAQItem(
            "How do I become a Top Contributor?",
            "Submit helpful complaints and get upvotes. Reach 50+ upvotes to earn the Top Contributor badge."
        ),
        FAQItem(
            "Is my personal information safe?",
            "Yes! Your data is encrypted and protected under privacy laws."
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // Top Navigation Bar - FIXED WITH PROPER INSETS
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
            shadowElevation = 4.dp,
            color = cardColor
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .clickable { onBackPressed() }
                        .size(28.dp),
                    tint = Color(0xFF1A237E)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Help & Support",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }
        }

        // Content Area - SCROLLABLE
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // FAQ SECTION
            Text(
                text = "Frequently Asked Questions",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column {
                    faqs.forEachIndexed { index, faq ->
                        ExpandableCardHelp(
                            question = faq.question,
                            answer = faq.answer,
                            isExpanded = expandedFAQ == index,
                            isDarkMode = isDarkMode,
                            textColor = textColor,
                            secondaryTextColor = secondaryTextColor,
                            onClick = { expandedFAQ = if (expandedFAQ == index) null else index }
                        )

                        if (index < faqs.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = 0.5.dp,
                                color = if (isDarkMode) Color(0xFF333333) else Color(0xFFEEEEEE)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // REPORTING GUIDELINES SECTION
            Text(
                text = "Reporting Guidelines",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            GuidelineCardHelp(
                title = "Do's ✓",
                content = listOf(
                    "Be specific - Include exact location",
                    "Be respectful - Use professional language",
                    "Provide context - Explain the problem",
                    "One issue per report",
                    "Include evidence - Photos/videos"
                ),
                isDarkMode = isDarkMode,
                cardColor = cardColor,
                textColor = textColor,
                secondaryTextColor = secondaryTextColor,
                isBad = false
            )

            Spacer(modifier = Modifier.height(12.dp))

            GuidelineCardHelp(
                title = "Don'ts ✗",
                content = listOf(
                    "Don't report personal disputes",
                    "Don't spam - Avoid duplicates",
                    "Don't be abusive",
                    "Don't use offensive language",
                    "Don't report false information"
                ),
                isDarkMode = isDarkMode,
                cardColor = cardColor,
                textColor = textColor,
                secondaryTextColor = secondaryTextColor,
                isBad = true
            )

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun ExpandableCardHelp(
    question: String,
    answer: String,
    isExpanded: Boolean,
    isDarkMode: Boolean,
    textColor: Color,
    secondaryTextColor: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = question,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = textColor,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color(0xFF1A237E),
                modifier = Modifier.size(24.dp)
            )
        }

        if (isExpanded) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = answer,
                fontSize = 12.sp,
                color = secondaryTextColor,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun GuidelineCardHelp(
    title: String,
    content: List<String>,
    isDarkMode: Boolean,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    isBad: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isBad) {
                if (isDarkMode) Color(0xFF3E1F1F) else Color(0xFFFCE8E8)
            } else {
                if (isDarkMode) Color(0xFF1F3E2C) else Color(0xFFE8FCF0)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isBad) Color(0xFFD32F2F) else Color(0xFF388E3C),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content.forEach { item ->
                Text(
                    text = "• $item",
                    fontSize = 12.sp,
                    color = secondaryTextColor,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HelpSupportPreview() {
    SmartCityTheme {
        HelpSupportScreenContent()
    }
}