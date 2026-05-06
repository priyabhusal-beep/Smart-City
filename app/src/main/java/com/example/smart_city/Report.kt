package com.example.smart_city

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.graphics.Color
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
fun Reportbody(){

    var search by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(color = Color.White)
            .padding(top = 90.dp) // Moves the content down
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
                modifier = Modifier.align(Alignment.CenterStart), // Positions SmartCity in the middle
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF1A237E) // Navy blue to match image
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
                Text("Location", fontSize = 12.sp, color = Color(0xFF1A237E), fontWeight = FontWeight.Bold)
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
                search=it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            placeholder={
                Text ("123 Urban Ave ,Metro City")
            },
            colors= TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Blue.copy(alpha=0.1f),
                focusedContainerColor = Color.Blue.copy(alpha=0.1f),
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
fun ReportPreview(){
    Reportbody()
}
