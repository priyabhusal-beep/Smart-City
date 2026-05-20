package com.example.smart_city

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smart_city.ui.theme.SmartCityTheme

val PrimaryBlue = Color(0xFF0046B1)
val AccentTeal = Color(0xFF00A389)
val LightBlueBg = Color(0xFFF0F5FF)
val BackgroundGray = Color(0xFFF8F9FA)

class HomeScreen : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            SmartCityTheme {
                HomeActivity()
            }
        }
    }
}

data class RecentReportData(
    val image: Int,
    val title: String,
    val location: String,
    val time: String,
    val status: String,
    val color: Color
)

@Composable
fun HomeActivity() {

    var search by remember {
        mutableStateOf("")
    }

    val recentReport = listOf(

        RecentReportData(
            image = R.drawable.road,
            title = "Broken Pavement",
            location = "12th Ave, North Block",
            time = "2h ago",
            status = "In Progress",
            color = PrimaryBlue
        ),

        RecentReportData(
            image = R.drawable.garbage,
            title = "Overflowing Bin",
            location = "Central Park East",
            time = "3h ago",
            status = "Pending",
            color = PrimaryBlue
        )
    )

    Scaffold(

        bottomBar = {

            Surface(
                shadowElevation = 8.dp,
                color = Color.White
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),

                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Icon(
                            painter = painterResource(
                                R.drawable.baseline_home_24
                            ),
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )

                        Text(
                            text = "Home",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }


                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Icon(
                            painter = painterResource(
                                R.drawable.baseline_report_24
                            ),
                            contentDescription = null,
                            tint = Color(0xFF1A237E),
                            modifier = Modifier.size(24.dp)
                        )

                        Text(
                            text = "Reports",
                            fontSize = 10.sp,
                            color = Color(0xFF1A237E),
                            fontWeight = FontWeight.Bold
                        )
                    }


                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Icon(
                            painter = painterResource(
                                R.drawable.baseline_person_outline_24
                            ),
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )

                        Text(
                            text = "Profile",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

    ) { innerPadding ->


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {

            // Header
            item {

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(
                            R.drawable.lana
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(
                                RoundedCornerShape(50.dp)
                            )
                    )

                    Spacer(
                        modifier = Modifier.width(12.dp)
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Good Morning",
                            color = Color.Gray
                        )

                        Text(
                            text = "Lana Del",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                    }

                    Icon(
                        painter = painterResource(
                            R.drawable.baseline_notifications_24
                        ),
                        contentDescription = null,
                        tint = PrimaryBlue
                    )
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )
            }


            // Search
            item {

                OutlinedTextField(
                    value = search,

                    onValueChange = {
                        search = it
                    },

                    modifier = Modifier.fillMaxWidth(),

                    placeholder = {
                        Text("Search complaints...")
                    },

                    leadingIcon = {

                        Icon(
                            painter = painterResource(
                                R.drawable.baseline_search_24
                            ),
                            contentDescription = null
                        )
                    },

                    shape = RoundedCornerShape(20.dp),

                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = Color.LightGray
                    )
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )
            }


            // Quick Actions
            item {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    ReportCard(
                        modifier = Modifier.weight(1f),
                        image = R.drawable.others,
                        label = "Report"
                    )

                    ReportCard(
                        modifier = Modifier.weight(1f),
                        image = R.drawable.track,
                        label = "Track"
                    )

                    ReportCard(
                        modifier = Modifier.weight(1f),
                        image = R.drawable.map,
                        label = "Nearby"
                    )
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )
            }


            // Categories
            item {

                Text(
                    text = "Categories",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )

                Spacer(
                    modifier = Modifier.height(15.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    ReportCard(
                        Modifier.weight(1f),
                        R.drawable.road,
                        "Road"
                    )

                    ReportCard(
                        Modifier.weight(1f),
                        R.drawable.garbage,
                        "Garbage"
                    )

                    ReportCard(
                        Modifier.weight(1f),
                        R.drawable.traffic,
                        "Traffic"
                    )

                    ReportCard(
                        Modifier.weight(1f),
                        R.drawable.map,
                        "Others"
                    )
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )
            }


            // Map
            item {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),

                    shape = RoundedCornerShape(18.dp)
                ) {

                    Box {

                        Image(
                            painter = painterResource(
                                R.drawable.mapp
                            ),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        Button(
                            onClick = {},

                            modifier = Modifier
                                .align(
                                    Alignment.BottomEnd
                                )
                                .padding(10.dp),

                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor =
                                        PrimaryBlue
                                )
                        ) {

                            Text(
                                "View Full Map"
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )
            }


            // Recent Report Title
            item {

                Row(
                    modifier = Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    Text(
                        text = "Recent Reports",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )

                    Text(
                        text = "View All",
                        color = Color.Gray
                    )
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )
            }


            // Dynamic items
            items(recentReport) { report ->

                RecentReportCard(report)

                Spacer(
                    modifier = Modifier.height(10.dp)
                )
            }

            item {
                Spacer(
                    modifier = Modifier.height(20.dp)
                )
            }
        }
    }
}


@Composable
fun ReportCard(
    modifier: Modifier,
    image: Int,
    label: String
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier.size(70.dp),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource(image),
                    contentDescription = null,
                    modifier = Modifier.size(70.dp)
                )
            }
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = label,
            fontSize = 13.sp,
            color = PrimaryBlue
        )
    }
}


@Composable
fun RecentReportCard(
    report: RecentReportData
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),

        shape = RoundedCornerShape(16.dp),

        elevation =
            CardDefaults.cardElevation(3.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Image(
                painter =
                    painterResource(report.image),

                contentDescription = null,

                modifier = Modifier
                    .size(60.dp)
                    .clip(
                        RoundedCornerShape(12.dp)
                    )
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = report.title,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )

                Text(
                    text = report.location,
                    color = Color.Gray,
                    fontSize = 13.sp
                )

                Text(
                    text = report.time,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            Text(
                text = report.status,
                color = report.color,
                fontSize = 12.sp,
                fontWeight =
                    FontWeight.SemiBold
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomePreview() {

    SmartCityTheme {
        HomeActivity()
    }
}