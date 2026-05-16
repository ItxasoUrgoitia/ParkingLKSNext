package com.lksnext.ParkingIUrgoitia.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.lksnext.ParkingIUrgoitia.data.MemoryDatabase
import com.lksnext.ParkingIUrgoitia.data.Reservation
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditReservationScreen(
    onNavigateHome: () -> Unit = {}
) {
    val lksOrange = Color(0xFFF25C05)

    var refreshKey by remember { mutableIntStateOf(0) }

    var reservationToEdit by remember { mutableStateOf<Reservation?>(null) }

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
    }.time

    val upcomingReservations = remember(refreshKey) {
        MemoryDatabase.reservationsList.filter {
            val resDate = try { dateFormat.parse(it.date) } catch (e: Exception) { null }
            resDate != null && !resDate.before(today)
        }
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Edit Reservation",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(onClick = { onNavigateHome() }) {
                        Icon(Icons.Default.Home, contentDescription = "Home", tint = lksOrange)
                    }

                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Person, contentDescription = "Profile", tint = lksOrange)
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
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            if (upcomingReservations.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No active reservations to edit.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(upcomingReservations) { reservation ->
                        EditReservationCard(
                            reservation = reservation,
                            lksOrange = lksOrange,
                            onEditClick = { reservationToEdit = reservation } // Abre el popup
                        )
                    }
                    item { Spacer(modifier = Modifier.height(32.dp)) }
                }
            }
        }
    }

    reservationToEdit?.let { res ->
        EditReservationDialog(
            reservation = res,
            lksOrange = lksOrange,
            onDismiss = { reservationToEdit = null }, // Cierra el popup sin guardar
            onSave = { updatedReservation ->
                val index = MemoryDatabase.reservationsList.indexOf(res)
                if (index != -1) {
                    MemoryDatabase.reservationsList[index] = updatedReservation
                }
                reservationToEdit = null
                refreshKey++
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditReservationDialog(
    reservation: Reservation,
    lksOrange: Color,
    onDismiss: () -> Unit,
    onSave: (Reservation) -> Unit
) {
    var date by remember { mutableStateOf(reservation.date) }
    var startTime by remember { mutableStateOf(reservation.startTime) }
    var endTime by remember { mutableStateOf(reservation.endTime) }
    var selectedVehicle by remember { mutableStateOf(reservation.vehicle) }
    var selectedSpot by remember { mutableStateOf(reservation.spot) }

    var vehicleExpanded by remember { mutableStateOf(false) }
    var spotExpanded by remember { mutableStateOf(false) }

    val vehicles = listOf("ABC-1234 (standard)", "XYZ-5678 (electric)")
    val allSpots = listOf("A1", "A2", "A3", "A4", "B1", "B2", "B3", "B4", "C1", "C2", "C3", "C4", "D1", "D2", "D3", "D4", "E1", "E2", "E3", "E4")

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = lksOrange,
        focusedLabelColor = lksOrange,
        cursorColor = lksOrange
    )

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    var showStartTimePicker by remember { mutableStateOf(false) }
    val startTimePickerState = rememberTimePickerState()

    var showEndTimePicker by remember { mutableStateOf(false) }
    val endTimePickerState = rememberTimePickerState()

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
                }) { Text("OK", color = lksOrange) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel", color = lksOrange) }
            }
        ) { DatePicker(state = datePickerState, colors = datePickerColors) } // <-- APLICADO AQUÍ
    }

    if (showStartTimePicker) {
        AlertDialog(
            onDismissRequest = { showStartTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showStartTimePicker = false
                    startTime = String.format(Locale.getDefault(), "%02d:%02d", startTimePickerState.hour, startTimePickerState.minute)
                }) { Text("OK", color = lksOrange) }
            },
            text = { TimePicker(state = startTimePickerState, colors = timePickerColors) } // <-- APLICADO AQUÍ
        )
    }

    if (showEndTimePicker) {
        AlertDialog(
            onDismissRequest = { showEndTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showEndTimePicker = false
                    endTime = String.format(Locale.getDefault(), "%02d:%02d", endTimePickerState.hour, endTimePickerState.minute)
                }) { Text("OK", color = lksOrange) }
            },
            text = { TimePicker(state = endTimePickerState, colors = timePickerColors) } // <-- APLICADO AQUÍ
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Text(text = "Edit Reservation", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Box {
                    OutlinedTextField(
                        value = date,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Date") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = textFieldColors,
                        trailingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) }
                    )
                    Spacer(modifier = Modifier.matchParentSize().background(Color.Transparent).clickable { showDatePicker = true })
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = startTime, onValueChange = {}, readOnly = true, label = { Text("Start Time") },
                            shape = RoundedCornerShape(8.dp), colors = textFieldColors,
                            trailingIcon = { Icon(Icons.Default.Schedule, contentDescription = null) }
                        )
                        Spacer(modifier = Modifier.matchParentSize().background(Color.Transparent).clickable { showStartTimePicker = true })
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = endTime, onValueChange = {}, readOnly = true, label = { Text("End Time") },
                            shape = RoundedCornerShape(8.dp), colors = textFieldColors,
                            trailingIcon = { Icon(Icons.Default.Schedule, contentDescription = null) }
                        )
                        Spacer(modifier = Modifier.matchParentSize().background(Color.Transparent).clickable { showEndTimePicker = true })
                    }
                }

                ExposedDropdownMenuBox(expanded = vehicleExpanded, onExpandedChange = { vehicleExpanded = !vehicleExpanded }) {
                    OutlinedTextField(
                        value = selectedVehicle, onValueChange = {}, readOnly = true, label = { Text("Vehicle") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = vehicleExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(), shape = RoundedCornerShape(8.dp), colors = textFieldColors
                    )
                    ExposedDropdownMenu(expanded = vehicleExpanded, onDismissRequest = { vehicleExpanded = false }, modifier = Modifier.background(Color.White)) {
                        vehicles.forEach { option ->
                            DropdownMenuItem(text = { Text(option) }, onClick = { selectedVehicle = option; vehicleExpanded = false })
                        }
                    }
                }

                ExposedDropdownMenuBox(expanded = spotExpanded, onExpandedChange = { spotExpanded = !spotExpanded }) {
                    OutlinedTextField(
                        value = selectedSpot, onValueChange = {}, readOnly = true, label = { Text("Parking Spot") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = spotExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(), shape = RoundedCornerShape(8.dp), colors = textFieldColors
                    )
                    ExposedDropdownMenu(
                        expanded = spotExpanded,
                        onDismissRequest = { spotExpanded = false },
                        modifier = Modifier.background(Color.White).heightIn(max = 200.dp)
                    ) {
                        allSpots.forEach { spotOption ->
                            DropdownMenuItem(text = { Text(spotOption) }, onClick = { selectedSpot = spotOption; spotExpanded = false })
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedReservation = Reservation(date, startTime, endTime, selectedVehicle, selectedSpot)
                    onSave(updatedReservation)
                },
                colors = ButtonDefaults.buttonColors(containerColor = lksOrange)
            ) {
                Text("Save Changes", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray, fontWeight = FontWeight.SemiBold)
            }
        }
    )
}

@Composable
fun EditReservationCard(
    reservation: Reservation,
    lksOrange: Color,
    onEditClick: () -> Unit
) {
    val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val outputFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.ENGLISH)
    val formattedDate = try {
        val parsedDate = inputFormat.parse(reservation.date)
        parsedDate?.let { outputFormat.format(it) } ?: reservation.date
    } catch (e: Exception) { reservation.date }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Parking Spot ${reservation.spot}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = lksOrange
                ) {
                    Text(
                        text = "upcoming",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = formattedDate, color = Color.DarkGray, fontSize = 14.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "${reservation.startTime} - ${reservation.endTime}", color = Color.DarkGray, fontSize = 14.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = reservation.vehicle, color = Color.DarkGray, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onEditClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = lksOrange)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Edit Reservation", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditReservationScreenPreview() {
    EditReservationScreen()
}