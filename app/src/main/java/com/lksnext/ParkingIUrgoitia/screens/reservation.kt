package com.lksnext.ParkingIUrgoitia.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Calendar
import com.lksnext.ParkingIUrgoitia.data.Reservation
import com.lksnext.ParkingIUrgoitia.data.MemoryDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationScreen(
    onNavigateHome: () -> Unit = {}
) {
    val lksOrange = Color(0xFFF25C05)
    val standardColor = Color(0xFF6B7280)
    val disabledColor = Color(0xFF3B82F6)
    val electricColor = Color(0xFF10B981)
    val motorbikeColor = Color(0xFF8B5CF6)
    val occupiedColor = Color(0xFFE5E7EB)

    var date by remember { mutableStateOf("dd/mm/aaaa") }
    var startTime by remember { mutableStateOf("--:--") }
    var endTime by remember { mutableStateOf("--:--") }
    var expanded by remember { mutableStateOf(false) }
    var selectedVehicle by remember { mutableStateOf("") }
    val vehicles = listOf("ABC-1234 (standard)", "XYZ-5678 (electric)")

    // Estados para el DatePicker
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // Estados para los TimePickers
    var showStartTimePicker by remember { mutableStateOf(false) }
    val startTimePickerState = rememberTimePickerState()

    var showEndTimePicker by remember { mutableStateOf(false) }
    val endTimePickerState = rememberTimePickerState()

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = lksOrange,
        focusedLabelColor = lksOrange,
        cursorColor = lksOrange
    )

    var selectedSpot by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // COLORES NARANJAS PARA LOS PICKERS
    val datePickerColors = DatePickerDefaults.colors(
        todayDateBorderColor = lksOrange,
        todayContentColor = lksOrange,
        selectedDayContainerColor = lksOrange,
        selectedDayContentColor = Color.White,
        currentYearContentColor = lksOrange,
        selectedYearContainerColor = lksOrange,
        selectedYearContentColor = Color.White
    )

    val timePickerColors = TimePickerDefaults.colors(
        selectorColor = lksOrange,
        timeSelectorSelectedContainerColor = lksOrange.copy(alpha = 0.2f),
        timeSelectorSelectedContentColor = lksOrange,
        timeSelectorUnselectedContainerColor = Color(0xFFF3F4F6),
        timeSelectorUnselectedContentColor = Color.Black,
        periodSelectorSelectedContainerColor = lksOrange.copy(alpha = 0.2f),
        periodSelectorSelectedContentColor = lksOrange,
        periodSelectorUnselectedContainerColor = Color.Transparent,
        periodSelectorUnselectedContentColor = Color.Black,
        clockDialColor = Color(0xFFF3F4F6),
        clockDialSelectedContentColor = Color.White,
        clockDialUnselectedContentColor = Color.Black
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    datePickerState.selectedDateMillis?.let { millis ->
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        date = formatter.format(Date(millis))
                    }
                }) {
                    Text("OK", color = lksOrange)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = lksOrange)
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = datePickerColors
            )
        }
    }

    if (showStartTimePicker) {
        AlertDialog(
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = { showStartTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showStartTimePicker = false
                    startTime = String.format(Locale.getDefault(), "%02d:%02d", startTimePickerState.hour, startTimePickerState.minute)
                }) {
                    Text("OK", color = lksOrange)
                }
            },
            dismissButton = {
                TextButton(onClick = { showStartTimePicker = false }) {
                    Text("Cancel", color = lksOrange)
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TimePicker(
                        state = startTimePickerState,
                        colors = timePickerColors // <-- APLICADO AQUÍ
                    )
                }
            }
        )
    }

    if (showEndTimePicker) {
        AlertDialog(
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = { showEndTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showEndTimePicker = false
                    endTime = String.format(Locale.getDefault(), "%02d:%02d", endTimePickerState.hour, endTimePickerState.minute)
                }) {
                    Text("OK", color = lksOrange)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndTimePicker = false }) {
                    Text("Cancel", color = lksOrange)
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TimePicker(
                        state = endTimePickerState,
                        colors = timePickerColors // <-- APLICADO AQUÍ
                    )
                }
            }
        )
    }

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
                        text = "Make a Reservation",
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
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Reservation Details",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFEFF6FF),
                        border = BorderStroke(1.dp, Color(0xFFBFDBFE))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "Info",
                                tint = Color(0xFF3B82F6),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Reservations must be within 7 days from today. Max duration: 9 hours.",
                                color = Color(0xFF1E3A8A),
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = date,
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Date") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = textFieldColors,
                            trailingIcon = {
                                IconButton(onClick = { showDatePicker = true }) {
                                    Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Calendar")
                                }
                            }
                        )
                        Spacer(
                            modifier = Modifier
                                .matchParentSize()
                                .background(Color.Transparent)
                                .clickable { showDatePicker = true }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = startTime,
                                onValueChange = { },
                                readOnly = true,
                                label = { Text("Start Time") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = textFieldColors,
                                trailingIcon = {
                                    IconButton(onClick = { showStartTimePicker = true }) {
                                        Icon(imageVector = Icons.Default.Schedule, contentDescription = "Clock")
                                    }
                                }
                            )
                            Spacer(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(Color.Transparent)
                                    .clickable { showStartTimePicker = true }
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = endTime,
                                onValueChange = { },
                                readOnly = true,
                                label = { Text("End Time") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = textFieldColors,
                                trailingIcon = {
                                    IconButton(onClick = { showEndTimePicker = true }) {
                                        Icon(imageVector = Icons.Default.Schedule, contentDescription = "Clock")
                                    }
                                }
                            )
                            Spacer(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(Color.Transparent)
                                    .clickable { showEndTimePicker = true }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedVehicle,
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Select Vehicle") },
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
                            vehicles.forEach { selectionOption ->
                                DropdownMenuItem(
                                    text = { Text(selectionOption) },
                                    onClick = {
                                        selectedVehicle = selectionOption
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Select Parking Spot",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Legend", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))

                    @Composable
                    fun LegendItem(color: Color, text: String, icon: ImageVector?) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = color,
                            modifier = Modifier.padding(end = 8.dp, bottom = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (icon != null) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = if (color == occupiedColor) Color.White else Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                Text(
                                    text = text,
                                    fontSize = 12.sp,
                                    color = if (color == occupiedColor) Color.White else Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                        LegendItem(standardColor, "Standard", Icons.Default.DirectionsCar)
                        LegendItem(disabledColor, "Disabled", Icons.Default.Accessible)
                        LegendItem(electricColor, "Electric", Icons.Default.Bolt)
                    }
                    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                        LegendItem(motorbikeColor, "Motorbike", Icons.Default.TwoWheeler)
                        LegendItem(occupiedColor, "Occupied", null)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    @Composable
                    fun SpotRow(spots: List<Triple<String, Color, ImageVector?>>) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            spots.forEach { spot ->
                                // Comprobamos si esta plaza ya está reservada en la fecha seleccionada
                                val isOccupied = MemoryDatabase.reservationsList.any {
                                    it.spot == spot.first && it.date == date
                                }
                                val actualColor = if (isOccupied) occupiedColor else spot.second
                                val isSelected = selectedSpot == spot.first && !isOccupied

                                Surface(
                                    onClick = {
                                        if (!isOccupied) {
                                            selectedSpot = spot.first
                                            errorMessage = ""
                                        }
                                    },
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (isSelected) lksOrange else actualColor,
                                    shadowElevation = if (isSelected) 6.dp else 0.dp,
                                    modifier = Modifier.size(60.dp)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        if (spot.third != null) {
                                            Icon(
                                                imageVector = spot.third!!,
                                                contentDescription = null,
                                                // Icono siempre en blanco para lograr el efecto de la foto
                                                tint = Color.White,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                        }
                                        Text(
                                            text = spot.first,
                                            // Texto siempre en blanco para lograr el efecto de la foto
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Column(modifier = Modifier.fillMaxWidth()) {
                        SpotRow(listOf(
                            Triple("A1", standardColor, Icons.Default.DirectionsCar),
                            Triple("A2", standardColor, Icons.Default.DirectionsCar),
                            Triple("A3", standardColor, Icons.Default.DirectionsCar),
                            Triple("A4", standardColor, Icons.Default.DirectionsCar)
                        ))
                        SpotRow(listOf(
                            Triple("B1", disabledColor, Icons.Default.Accessible),
                            Triple("B2", disabledColor, Icons.Default.Accessible),
                            Triple("B3", standardColor, Icons.Default.DirectionsCar),
                            Triple("B4", standardColor, Icons.Default.DirectionsCar)
                        ))
                        SpotRow(listOf(
                            Triple("C1", electricColor, Icons.Default.Bolt),
                            Triple("C2", electricColor, Icons.Default.Bolt),
                            Triple("C3", electricColor, Icons.Default.Bolt),
                            Triple("C4", standardColor, Icons.Default.DirectionsCar)
                        ))
                        SpotRow(listOf(
                            Triple("D1", motorbikeColor, Icons.Default.TwoWheeler),
                            Triple("D2", motorbikeColor, Icons.Default.TwoWheeler),
                            Triple("D3", standardColor, Icons.Default.DirectionsCar),
                            Triple("D4", standardColor, Icons.Default.DirectionsCar)
                        ))
                        SpotRow(listOf(
                            Triple("E1", standardColor, Icons.Default.DirectionsCar),
                            Triple("E2", standardColor, Icons.Default.DirectionsCar),
                            Triple("E3", standardColor, Icons.Default.DirectionsCar),
                            Triple("E4", standardColor, Icons.Default.DirectionsCar)
                        ))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = {
                    if (date == "dd/mm/aaaa" || startTime == "--:--" || endTime == "--:--" ||
                        selectedVehicle.isEmpty() || selectedSpot.isEmpty()) {

                        errorMessage = "Please fill all fields and select a parking spot."
                    } else {
                        try {
                            val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            val selectedDate = dateFormatter.parse(date)

                            val calendar = Calendar.getInstance()
                            calendar.set(Calendar.HOUR_OF_DAY, 0)
                            calendar.set(Calendar.MINUTE, 0)
                            calendar.set(Calendar.SECOND, 0)
                            calendar.set(Calendar.MILLISECOND, 0)
                            val today = calendar.time

                            calendar.add(Calendar.DAY_OF_YEAR, 7)
                            val maxDate = calendar.time

                            if (selectedDate != null && selectedDate.before(today)) {
                                errorMessage = "Reservation date cannot be in the past."
                            } else if (selectedDate != null && selectedDate.after(maxDate)) {
                                errorMessage = "Reservations must be within 7 days from today."
                            } else {
                                val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
                                val start = timeFormatter.parse(startTime)
                                val end = timeFormatter.parse(endTime)

                                if (start != null && end != null) {
                                    var diff = end.time - start.time

                                    if (diff < 0) {
                                        diff += 24 * 60 * 60 * 1000
                                    }

                                    val durationInHours = diff / (1000.0 * 60.0 * 60.0)

                                    if (durationInHours > 9.0) {
                                        errorMessage = "Maximum duration is 9 hours."
                                    } else if (durationInHours == 0.0) {
                                        errorMessage = "Start and end times cannot be the same."
                                    } else {
                                        errorMessage = ""
                                        val newReservation = Reservation(
                                            date = date,
                                            startTime = startTime,
                                            endTime = endTime,
                                            vehicle = selectedVehicle,
                                            spot = selectedSpot
                                        )

                                        MemoryDatabase.reservationsList.add(newReservation)
                                        onNavigateHome()
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            errorMessage = "Error validating date or time format."
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = lksOrange)
            ) {
                Text(
                    text = "Confirm Reservation",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReservationScreenPreview() {
    ReservationScreen()
}