package com.lksnext.ParkingIUrgoitia.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCarScreen(
    onNavigateHome: () -> Unit = {}
) {
    val lksOrange = Color(0xFFF25C05)

    var licensePlate by remember { mutableStateOf("") }
    var vehicleType by remember { mutableStateOf("Car - Standard") }
    var expanded by remember { mutableStateOf(false) }

    val vehicleOptions = listOf(
        "Car - Standard" to Icons.Default.DirectionsCar,
        "Car - Electric" to Icons.Default.Bolt,
        "Motorbike" to Icons.Default.TwoWheeler,
        "Disabled" to Icons.Default.Accessible
    )

    val selectedIcon = vehicleOptions.find { it.first == vehicleType }?.second ?: Icons.Default.DirectionsCar

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = lksOrange,
        focusedLabelColor = lksOrange,
        cursorColor = lksOrange
    )

    Scaffold(
        topBar = {
            Surface(
                shadowElevation = 3.dp,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onNavigateHome() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Add Vehicle",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(onClick = { onNavigateHome() }) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            tint = lksOrange
                        )
                    }

                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = lksOrange
                        )
                    }
                }
            }
        },
        containerColor = Color(0xFFFAFAFA)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = lksOrange,
                        modifier = Modifier.size(72.dp),
                        shadowElevation = 4.dp
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = "Car Icon",
                            tint = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Vehicle Details",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Add a new vehicle to your account",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    OutlinedTextField(
                        value = licensePlate,
                        onValueChange = { licensePlate = it },
                        label = { Text("License Plate") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = textFieldColors,
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = vehicleType,
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Vehicle Type") },
                            leadingIcon = {
                                Icon(
                                    imageVector = selectedIcon,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(8.dp),
                            colors = textFieldColors
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            vehicleOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option.first) },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = option.second,
                                            contentDescription = null,
                                            tint = Color.Gray
                                        )
                                    },
                                    onClick = {
                                        vehicleType = option.first
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = lksOrange)
                    ) {
                        Text(
                            text = "Add Vehicle",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddCarScreenPreview() {
    AddCarScreen()
}