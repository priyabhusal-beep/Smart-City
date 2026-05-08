package com.example.smart_city

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField


import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    Column(

        modifier = Modifier.fillMaxSize()
            .background(color = Color.White)
            .padding(top = 20.dp)
            .verticalScroll(scrollState)
    )
    {
        // --- EXISTING CODE ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "SmartCity",
                modifier = Modifier.align(Alignment.CenterStart),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF1A237E)
                )
            )

            Icon(
                painter = painterResource(R.drawable.baseline_notifications_none_24),
                contentDescription = "Notifications",
                modifier = Modifier.align(Alignment.CenterEnd),
                tint = Color(0xFF1A237E)
            )
        }
        // --- END OF EXISTING CODE ---

        Spacer(modifier = Modifier.height(32.dp))

        // Stepper section
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StepCircle("1", isSelected = true)
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE0E0E0))
                StepCircle("2", isSelected = false)
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE0E0E0))
                StepCircle("3", isSelected = false)
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Location",
                    fontSize = 12.sp,
                    color = Color(0xFF1A237E),
                    fontWeight = FontWeight.Bold
                )
                Text("Details", fontSize = 12.sp, color = Color.Gray)
                Text("Review", fontSize = 12.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Incident Location Title and Detect My Location Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "INCIDENT LOCATION",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
//                Icon(
//                    imageVector = Icons.Default.MyLocation,
//                    contentDescription = "Detect",
//                    tint = Color(0xFF1E88E5),
//                    modifier = Modifier.size(16.dp)
//                )
                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "Detect My Location",
                    style = TextStyle(
                        fontSize = 13.sp,
                        color = Color(0xFF1E88E5),
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = search,
            onValueChange = {
                search = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            placeholder = {
                Text("123 Urban Ave ,Metro City")
            },
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color(0xFFE0E0E0),
                unfocusedContainerColor = Color.Blue.copy(alpha = 0.1f),
                focusedContainerColor = Color.Blue.copy(alpha = 0.1f),
                focusedIndicatorColor = Color.Blue,

                ),

            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFF1A237E)

                )
            },
            shape = RoundedCornerShape(8.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(200.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0))


        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(
                    text = "Map placeholder", style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )

            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,


            ) {
            Text(
                text = "Visual Evidence", style = TextStyle(
                    fontSize = 13.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                )
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),) {
            Card(
                modifier = Modifier.size(110.dp)
                    .drawBehind {
                        val strokeWidth = 4f
                        drawRoundRect(
                            color = Color.LightGray,
                            style = Stroke(
                                width = 2F,
                                pathEffect = PathEffect.dashPathEffect(
                                    floatArrayOf(10f, 10f), 0f
                                )
                            )
                        )
                    },


                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)


            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_add_a_photo_24),
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Add Photo",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,

                            )
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Card(
                modifier = Modifier.size(110.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Image(
                        painter = painterResource(R.drawable.baseline_image_24),
                        contentDescription = null
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp),

            ) {
            Text(
                text = " ISSUE DESCRIPTION", style = TextStyle(
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                },
                modifier = Modifier.fillMaxWidth()
                    .height(120.dp),
                placeholder = {
                    Text(text = "Describe the issue in detail...")
                },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White


                )

            )


        }

        Spacer(modifier = Modifier.height(32.dp))
        ElevatedButton(
            onClick = {  },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = Color(0xFF0D2A77),
                contentColor = Color.White
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Submit Report", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(R.drawable.baseline_arrow_forward_24),
                    contentDescription = null
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))


        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(R.drawable.baseline_home_24),
                    contentDescription = null,
                    tint = Color.Gray
                )
                Text(text = "Home", fontSize = 10.sp, color = Color.Gray)
            }


            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(R.drawable.baseline_report_24),
                    contentDescription = null,
                    tint = Color(0xFF1A237E)
                )
                Text(
                    text = "Reports", 
                    fontSize = 10.sp, 
                    color = Color(0xFF1A237E),
                    fontWeight = FontWeight.Bold
                )
            }


            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(R.drawable.baseline_person_outline_24),
                    contentDescription = null,
                    tint = Color.Gray
                )
                Text(text = "Profile", fontSize = 10.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun StepCircle(number: String, isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .background(
                color = if (isSelected) Color(0xFF1A237E) else Color(0xFFF5F5F5),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number,
            color = if (isSelected) Color.White else Color.Gray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReportPreview() {
    Reportbody()
}
