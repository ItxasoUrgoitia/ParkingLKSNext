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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.lksnext.ParkingIUrgoitia.viewmodels.ReservationViewModel
import com.lksnext.ParkingIUrgoitia.viewmodels.ReservationUiState
import com.lksnext.ParkingIUrgoitia.viewmodels.ParkingSpotState
import com.lksnext.ParkingIUrgoitia.data.SpotType
import androidx.compose.material.icons.automirrored.filled.Accessible


private val lksOrange = Color(0xFFF25C05)
private val standardColor = Color(0xFF6B7280)
private val disabledColor = Color(0xFF3B82F6)
private val electricColor = Color(0xFF10B981)
private val motorbikeColor = Color(0xFF8B5CF6)
private val occupiedColor = Color(0xFFE5E7EB)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationScreen(
    viewModel: ReservationViewModel,
    onNavigateHome: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val vehicles = listOf("ABC-1234 (standard)", "XYZ-5678 (electric)")


    var showDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onNavigateHome()
    }


    if (showDatePicker) {
        ReservationDatePicker(
            onDismiss = { showDatePicker = false },
            onDateSelected = { viewModel.onDateSelected(it) }
        )
    }

    if (showStartTimePicker) {
        ReservationTimePicker(
            onDismiss = { showStartTimePicker = false },
            onTimeSelected = { viewModel.onStartTimeSelected(it) }
        )
    }

    if (showEndTimePicker) {
        ReservationTimePicker(
            onDismiss = { showEndTimePicker = false },
            onTimeSelected = { viewModel.onEndTimeSelected(it) }
        )
    }

    Scaffold(
        topBar = { ReservationTopBar(onNavigateHome) },
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


            ReservationDetailsCard(
                uiState = uiState,
                vehicles = vehicles,
                onShowDatePicker = { showDatePicker = true },
                onShowStartTimePicker = { showStartTimePicker = true },
                onShowEndTimePicker = { showEndTimePicker = true },
                onVehicleSelected = { viewModel.onVehicleSelected(it) }
            )

            Spacer(modifier = Modifier.height(24.dp))


            ParkingSpotSelectionCard(
                spots = uiState.spots,
                selectedSpotId = uiState.selectedSpot,
                onSpotSelected = { viewModel.onSpotSelected(it) }
            )

            Spacer(modifier = Modifier.height(24.dp))


            SubmitSection(
                errorMessage = uiState.errorMessage,
                onSubmit = { viewModel.validateAndSaveReservation() }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReservationDatePicker(
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDismiss()
                datePickerState.selectedDateMillis?.let { millis ->
                    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    onDateSelected(formatter.format(Date(millis)))
                }
            }) { Text("OK", color = lksOrange) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel", color = lksOrange) } }
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReservationTimePicker(
    onDismiss: () -> Unit,
    onTimeSelected: (String) -> Unit
) {
    val timePickerState = rememberTimePickerState()
    val timePickerColors = TimePickerDefaults.colors(
        selectorColor = lksOrange, timeSelectorSelectedContainerColor = lksOrange.copy(alpha = 0.2f),
        timeSelectorSelectedContentColor = lksOrange, timeSelectorUnselectedContainerColor = Color(0xFFF3F4F6)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDismiss()
                val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", timePickerState.hour, timePickerState.minute)
                onTimeSelected(formattedTime)
            }) { Text("OK", color = lksOrange) }
        },
        text = { TimePicker(state = timePickerState, colors = timePickerColors) }
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReservationTopBar(onNavigateHome: () -> Unit) {
    Surface(shadowElevation = 3.dp, color = Color.White, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateHome) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Make a Reservation", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onNavigateHome) { Icon(Icons.Default.Home, "Home", tint = lksOrange) }
            IconButton(onClick = { }) { Icon(Icons.Default.Person, "Profile", tint = lksOrange) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReservationDetailsCard(
    uiState: ReservationUiState,
    vehicles: List<String>,
    onShowDatePicker: () -> Unit,
    onShowStartTimePicker: () -> Unit,
    onShowEndTimePicker: () -> Unit,
    onVehicleSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = lksOrange, focusedLabelColor = lksOrange, cursorColor = lksOrange
    )

    Surface(shape = RoundedCornerShape(12.dp), color = Color.White, shadowElevation = 2.dp) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Reservation Details", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = uiState.date, onValueChange = { }, readOnly = true, label = { Text("Date") },
                    modifier = Modifier.fillMaxWidth(), colors = textFieldColors,
                    trailingIcon = { IconButton(onClick = onShowDatePicker) { Icon(Icons.Default.CalendarToday, null) } }
                )
                Spacer(modifier = Modifier.matchParentSize().background(Color.Transparent).clickable(onClick = onShowDatePicker))
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = uiState.startTime, onValueChange = { }, readOnly = true, label = { Text("Start Time") },
                        modifier = Modifier.fillMaxWidth(), colors = textFieldColors
                    )
                    Spacer(modifier = Modifier.matchParentSize().background(Color.Transparent).clickable(onClick = onShowStartTimePicker))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = uiState.endTime, onValueChange = { }, readOnly = true, label = { Text("End Time") },
                        modifier = Modifier.fillMaxWidth(), colors = textFieldColors
                    )
                    Spacer(modifier = Modifier.matchParentSize().background(Color.Transparent).clickable(onClick = onShowEndTimePicker))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = uiState.selectedVehicle, onValueChange = { }, readOnly = true, label = { Text("Select Vehicle") },
                    modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    colors = textFieldColors,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    vehicles.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = { onVehicleSelected(selectionOption); expanded = false }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ParkingSpotSelectionCard(
    spots: List<ParkingSpotState>,
    selectedSpotId: String,
    onSpotSelected: (String) -> Unit
) {
    Surface(shape = RoundedCornerShape(12.dp), color = Color.White, shadowElevation = 2.dp) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Select Parking Spot", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))

            ParkingSpotLegend()
            Spacer(modifier = Modifier.height(24.dp))

            ParkingSpotGrid(spots, selectedSpotId, onSpotSelected)
        }
    }
}

@Composable
private fun ParkingSpotGrid(
    spots: List<ParkingSpotState>,
    selectedSpotId: String,
    onSpotSelected: (String) -> Unit
) {
    val chunkedSpots = spots.chunked(4)
    Column(modifier = Modifier.fillMaxWidth()) {
        chunkedSpots.forEach { rowSpots ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                rowSpots.forEach { spot ->
                    ParkingSpotItem(
                        spot = spot,
                        isSelected = selectedSpotId == spot.id,
                        onClick = { onSpotSelected(spot.id) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ParkingSpotItem(
    spot: ParkingSpotState,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val baseColor = when {
        spot.isOccupied -> occupiedColor
        isSelected -> lksOrange
        else -> when (spot.type) {
            SpotType.STANDARD -> standardColor
            SpotType.DISABLED -> disabledColor
            SpotType.ELECTRIC -> electricColor
            SpotType.MOTORBIKE -> motorbikeColor
        }
    }

    val spotIcon = when (spot.type) {
        SpotType.STANDARD -> Icons.Default.DirectionsCar
        SpotType.DISABLED -> Icons.AutoMirrored.Filled.Accessible
        SpotType.ELECTRIC -> Icons.Default.Bolt
        SpotType.MOTORBIKE -> Icons.Default.TwoWheeler
    }

    Surface(
        onClick = { if (!spot.isOccupied) onClick() },
        shape = RoundedCornerShape(16.dp),
        color = baseColor,
        modifier = Modifier.size(60.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            if (!spot.isOccupied) {
                Icon(imageVector = spotIcon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.height(4.dp))
            }
            Text(text = spot.id, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}

@Composable
private fun ParkingSpotLegend() {
    Text(text = "Legend", fontSize = 12.sp, color = Color.Gray)
    Spacer(modifier = Modifier.height(8.dp))
    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
        LegendItem(standardColor, "Standard", Icons.Default.DirectionsCar)
        LegendItem(disabledColor, "Disabled", Icons.AutoMirrored.Filled.Accessible)
        LegendItem(electricColor, "Electric", Icons.Default.Bolt)
    }
    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
        LegendItem(motorbikeColor, "Motorbike", Icons.Default.TwoWheeler)
        LegendItem(occupiedColor, "Occupied", null)
    }
}

@Composable
private fun LegendItem(color: Color, text: String, icon: ImageVector?) {
    Surface(shape = RoundedCornerShape(16.dp), color = color, modifier = Modifier.padding(end = 8.dp, bottom = 8.dp)) {
        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(text = text, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun SubmitSection(errorMessage: String, onSubmit: () -> Unit) {
    if (errorMessage.isNotEmpty()) {
        Text(text = errorMessage, color = Color.Red, fontSize = 14.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 8.dp))
    }
    Button(
        onClick = onSubmit,
        modifier = Modifier.fillMaxWidth().height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = lksOrange)
    ) {
        Text("Confirm Reservation", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}