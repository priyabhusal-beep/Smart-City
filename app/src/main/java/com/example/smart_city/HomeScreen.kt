package com.example.smart_city

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    val image : Int,
    val title : String,
    val location: String,
    val time: String,
    val status: String,
    val color : Color
)
@Composable
fun HomeActivity(){
    var search by remember { mutableStateOf("") }
    val recentReport = listOf(
        RecentReportData(
            image = R.drawable.road,
            "Broken Pavement",
            location = "12th Ave, North Block",
            time = "2h ago",
            status = "In Progress",
            color = PrimaryBlue
        ),
        RecentReportData(
            image = R.drawable.garbage, // replace with your image
            title = "Overflowing Bin",
            location = "Central Park East",
            time = "3h ago",
            status = "Pending",
            color = PrimaryBlue
        )
    )
    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
            .padding(35.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.lana),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp)
                        .clip(RoundedCornerShape(50.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Good Morning",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Lana del",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )
                }

                Icon(
                    painter = painterResource(R.drawable.baseline_notifications_none_24),
                    contentDescription = null,
                    tint = PrimaryBlue
                )
            }
            Spacer(modifier = Modifier.height(15.dp))

            // ================= SEARCH BAR =================

            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search complaints..", color = Color.Gray) },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = PrimaryBlue
                ),
                leadingIcon = { Icon(painter = painterResource(R.drawable.baseline_search_24),
                        contentDescription = null
                    )
                },
                shape = RoundedCornerShape(20.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))

            // ================= QUICK ACTIONS =================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                ReportCard(
                    modifier = Modifier.weight(1f),
                    image = R.drawable.others,
                    label = "Report issue"
                )
                ReportCard(
                    modifier = Modifier.weight(1f),
                    image = R.drawable.track,
                    label = "Track Issue"
                )

                ReportCard(
                    modifier = Modifier.weight(1f),
                    image = R.drawable.map,
                    label = "Nearby"
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Categories",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
            Spacer(modifier = Modifier.height(15.dp))

// ================= CATEGORY TITLE =================

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                ReportCard(
                    modifier = Modifier.weight(1f),
                    image = R.drawable.road,
                    label = "Road"
                )

                ReportCard(
                    modifier = Modifier.weight(1f),
                    image = R.drawable.garbage,
                    label = "Garbage"
                )

                ReportCard(
                    modifier = Modifier.weight(1f),
                    image = R.drawable.traffic,
                    label = "Traffic"
                )

                ReportCard(
                    modifier = Modifier.weight(1f),
                    image = R.drawable.map,
                    label = "Others"
                )
            }


            Spacer(modifier = Modifier.height(20.dp))

            //==================== MAP SECTION ====================
            Card(
                modifier = Modifier.fillMaxWidth().height(160.dp),
                shape = RoundedCornerShape(18.dp)
            ) {
                Box {
                    Image(
                        painter = painterResource(R.drawable.mapp), // Add a map screenshot to drawable
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Button(
                        onClick = {},
                        modifier = Modifier.align(Alignment.BottomEnd).padding(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("View Full Map", fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))


            //==================== RECENT REPORT TITLE ====================

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
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


            Spacer(modifier = Modifier.height(12.dp))


            //==================== RECENT REPORT ITEMS ====================

            recentReport.forEach {

                RecentReportCard(it)

                Spacer(modifier = Modifier.height(12.dp))

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
    ) {Card(
        modifier = Modifier.size(70.dp),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier.size(45.dp)
            )
        }
    }
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            fontSize = 13.sp,
            color = PrimaryBlue
        )
    }
}


@Composable
fun RecentReportCard(
    report : RecentReportData
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(report.image),
                contentDescription = null,

                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
            )


            Spacer(modifier = Modifier.width(12.dp))


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
                color = Color.Blue,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true )
@Composable
fun HomePreview(){
    SmartCityTheme {
        HomeActivity()
    }

}