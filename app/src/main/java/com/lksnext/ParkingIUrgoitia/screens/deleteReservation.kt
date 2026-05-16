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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lksnext.ParkingIUrgoitia.data.MemoryDatabase
import com.lksnext.ParkingIUrgoitia.data.Reservation
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteReservationScreen(
    onNavigateHome: () -> Unit = {}
) {
    val lksOrange = Color(0xFFF25C05)

    var refreshKey by remember { mutableIntStateOf(0) }

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
                        text = "Delete Reservation",
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
                    Text(text = "No active reservations available to delete.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(upcomingReservations) { reservation ->
                        DeleteReservationCard(
                            reservation = reservation,
                            lksOrange = lksOrange,
                            onDeleteClick = {
                                MemoryDatabase.reservationsList.remove(reservation)
                                refreshKey++
                            }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(32.dp)) }
                }
            }
        }
    }
}

@Composable
fun DeleteReservationCard(
    reservation: Reservation,
    lksOrange: Color,
    onDeleteClick: () -> Unit
) {
    val deleteRed = Color(0xFFEF4444) // Rojo estilo Material Design

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
            HorizontalDivider(color = Color(0xFFF3F4F6), thickness = 1.dp)
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

            OutlinedButton(
                onClick = onDeleteClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, deleteRed),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = deleteRed)
            ) {
                Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Delete Reservation", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeleteReservationScreenPreview() {
    DeleteReservationScreen()
}