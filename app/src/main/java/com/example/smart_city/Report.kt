package com.example.smart_city
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class Report : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
           Reportbody()
        }
    }
}

@Composable
fun Reportbody() {
    var search by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Scaffold(
        bottomBar = {

            Surface(
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(painterResource(R.drawable.baseline_home_24),
                            null,
                            tint = Color.Gray,
                            modifier = Modifier.size(24.dp))
                        Text(text = "Home", fontSize = 10.sp, color = Color.Gray)
                    }


                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(painterResource(R.drawable.baseline_report_24),
                            null,
                            tint = Color(0xFF1A237E),
                            modifier = Modifier.size(24.dp))
                        Text(text = "Reports", fontSize = 10.sp, color = Color(0xFF1A237E), fontWeight = FontWeight.Bold)
                    }


                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(painterResource(R.drawable.baseline_person_outline_24), null, tint = Color.Gray, modifier = Modifier.size(24.dp))
                        Text(text = "Profile", fontSize = 10.sp, color = Color.Gray)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = Color.White)
                .verticalScroll(scrollState)
                .padding(top = 20.dp)
        ) {

            Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Text(
                    text = "SmartCity",
                    modifier = Modifier.align(Alignment.CenterStart),
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF1A237E))
                )
                Icon(
                    painter = painterResource(R.drawable.baseline_notifications_none_24),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd),
                    tint = Color(0xFF1A237E)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))


            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    StepCircle("1", isSelected = true)
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE0E0E0))
                    StepCircle("2", isSelected = false)
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE0E0E0))
                    StepCircle("3", isSelected = false)
                }
                Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Location", fontSize = 12.sp, color = Color(0xFF1A237E), fontWeight = FontWeight.Bold)
                    Text("Details", fontSize = 12.sp, color = Color.Gray)
                    Text("Review", fontSize = 12.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))


            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically) {
                Text("INCIDENT LOCATION", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Gray))
                Text("Detect My Location", style = TextStyle(fontSize = 13.sp, color = Color(0xFF1E88E5), fontWeight = FontWeight.Bold))
            }

            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                placeholder = { Text("123 Urban Ave, Metro City") },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Blue.copy(alpha = 0.05f),
                    focusedContainerColor = Color.Blue.copy(alpha = 0.05f),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Blue
                ),
                trailingIcon = { Icon(Icons.Default.Search,
                    null,
                    tint = Color(0xFF1A237E)) },
                shape = RoundedCornerShape(8.dp)
            )


            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(200.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0))
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Map placeholder", fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }


            Text("Visual Evidence", modifier = Modifier.padding(horizontal = 18.dp), style = TextStyle(fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Card(
                    modifier = Modifier.size(110.dp).drawBehind {
                        drawRoundRect(color = Color.LightGray, style = Stroke(width = 2F, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)))
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Column(modifier = Modifier.fillMaxSize(),
                        Arrangement.Center,
                        Alignment.CenterHorizontally) {
                        Icon(painterResource(R.drawable.baseline_add_a_photo_24), null, tint = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Add Photo", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Card(modifier = Modifier.size(110.dp), shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0))) {
                    Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
                        Image(painterResource(R.drawable.baseline_image_24), null, Modifier.size(40.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Text("ISSUE DESCRIPTION", style = TextStyle(fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    placeholder = { Text("Describe the issue in detail...") },
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.White, focusedContainerColor = Color.White)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))


            ElevatedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.elevatedButtonColors(containerColor = Color(0xFF0D2A77), contentColor = Color.White)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Submit Report", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(painterResource(R.drawable.baseline_arrow_forward_24), null)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun StepCircle(number: String, isSelected: Boolean) {
    Box(
        modifier = Modifier.size(32.dp).background(color = if (isSelected) Color(0xFF1A237E) else Color(0xFFF5F5F5), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(text = number, color = if (isSelected) Color.White else Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun ReportPreview() {
    Reportbody()
}
