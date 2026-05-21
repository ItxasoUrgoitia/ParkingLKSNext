package com.lksnext.ParkingIUrgoitia.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lksnext.ParkingIUrgoitia.data.Reservation
import com.lksnext.ParkingIUrgoitia.viewmodels.DeleteReservationViewModel
import java.text.SimpleDateFormat
import java.util.Locale

private val lksOrange = Color(0xFFF25C05)
private val deleteRed = Color(0xFFEF4444) // Rojo estilo Material Design

@Composable
fun DeleteReservationScreen(
    viewModel: DeleteReservationViewModel,
    onNavigateHome: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    // Se recarga la lista cada vez que entramos a la pantalla
    LaunchedEffect(Unit) {
        viewModel.loadUpcomingReservations()
    }

    Scaffold(
        topBar = { DeleteReservationTopBar(onNavigateHome) },
        containerColor = Color(0xFFFAFAFA)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.upcomingReservations.isEmpty()) {
                EmptyDeleteMessage()
            } else {
                DeleteReservationsList(
                    reservations = uiState.upcomingReservations,
                    onDeleteClick = { reservation -> viewModel.deleteReservation(reservation) }
                )
            }
        }
    }
}


@Composable
private fun DeleteReservationTopBar(onNavigateHome: () -> Unit) {
    Surface(shadowElevation = 3.dp, color = Color.White, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateHome) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Delete Reservation", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onNavigateHome) { Icon(Icons.Default.Home, contentDescription = "Home", tint = lksOrange) }
            IconButton(onClick = { }) { Icon(Icons.Default.Person, contentDescription = "Profile", tint = lksOrange) }
        }
    }
}

@Composable
private fun EmptyDeleteMessage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "No active reservations available to delete.", color = Color.Gray, fontSize = 16.sp)
    }
}

@Composable
private fun DeleteReservationsList(
    reservations: List<Reservation>,
    onDeleteClick: (Reservation) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(reservations) { reservation ->
            DeleteReservationCard(
                reservation = reservation,
                onDeleteClick = { onDeleteClick(reservation) }
            )
        }
        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

@Composable
private fun DeleteReservationCard(
    reservation: Reservation,
    onDeleteClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            CardHeader(spot = reservation.spot)

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFF3F4F6), thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                DeleteReservationDetailRow(icon = Icons.Default.CalendarToday, text = formatReservationDate(reservation.date))
                DeleteReservationDetailRow(icon = Icons.Default.Schedule, text = "${reservation.startTime} - ${reservation.endTime}")
                DeleteReservationDetailRow(icon = Icons.Default.DirectionsCar, text = reservation.vehicle)
            }

            Spacer(modifier = Modifier.height(20.dp))
            DeleteButton(onDeleteClick = onDeleteClick)
        }
    }
}

@Composable
private fun CardHeader(spot: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Parking Spot $spot", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
        Surface(shape = RoundedCornerShape(16.dp), color = lksOrange) {
            Text(
                text = "upcoming", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun DeleteReservationDetailRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, color = Color.DarkGray, fontSize = 14.sp)
    }
}

@Composable
private fun DeleteButton(onDeleteClick: () -> Unit) {
    OutlinedButton(
        onClick = onDeleteClick,
        modifier = Modifier.fillMaxWidth().height(48.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, deleteRed),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = deleteRed)
    ) {
        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Delete Reservation", fontWeight = FontWeight.Bold)
    }
}

// Función para formatear la fecha de reserva a un formato más legible
private fun formatReservationDate(originalDate: String): String {
    val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val outputFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.ENGLISH)
    return try {
        val parsedDate = inputFormat.parse(originalDate)
        parsedDate?.let { outputFormat.format(it) } ?: originalDate
    } catch (e: Exception) {
        originalDate
    }
}