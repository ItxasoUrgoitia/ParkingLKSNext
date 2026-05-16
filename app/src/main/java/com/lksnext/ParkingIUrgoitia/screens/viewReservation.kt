package com.lksnext.ParkingIUrgoitia.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
fun ViewReservationsScreen(
    onNavigateHome: () -> Unit = {}
) {
    val lksOrange = Color(0xFFF25C05)

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Active", "History")

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

    val activeReservations = MemoryDatabase.reservationsList.filter {
        val resDate = dateFormat.parse(it.date)
        resDate != null && !resDate.before(today) // Si la fecha NO es anterior a hoy
    }

    val historyReservations = MemoryDatabase.reservationsList.filter {
        val resDate = dateFormat.parse(it.date)
        resDate != null && resDate.before(today) // Si la fecha SÍ es anterior a hoy
    }

    val displayedReservations = if (selectedTabIndex == 0) activeReservations else historyReservations

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
                        text = "View Reservations",
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
        ) {
            // Construcción de las pestañas (Tabs)
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                indicator = { tabPositions ->
                    if (selectedTabIndex < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = lksOrange
                        )
                    }
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                color = if (selectedTabIndex == index) lksOrange else Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (displayedReservations.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No reservations found.",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(displayedReservations) { reservation ->
                        ReservationCard(
                            reservation = reservation,
                            isHistory = selectedTabIndex == 1,
                            lksOrange = lksOrange
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ReservationCard(reservation: Reservation, isHistory: Boolean, lksOrange: Color) {
    val pillBgColor = if (isHistory) Color(0xFFE5E7EB) else lksOrange
    val pillTextColor = if (isHistory) Color.DarkGray else Color.White
    val pillText = if (isHistory) "completed" else "upcoming"

    val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val outputFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.ENGLISH)

    val formattedDate = try {
        val parsedDate = inputFormat.parse(reservation.date)
        parsedDate?.let { outputFormat.format(it) } ?: reservation.date
    } catch (e: Exception) {
        reservation.date
    }

    val spotType = when (reservation.spot.firstOrNull()) {
        'B' -> "disabled"
        'C' -> "electric"
        'D' -> "motorbike"
        else -> "standard"
    }

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
                    color = pillBgColor
                ) {
                    Text(
                        text = pillText,
                        color = pillTextColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFF3F4F6), thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = formattedDate, color = Color.DarkGray, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "${reservation.startTime} - ${reservation.endTime}", color = Color.DarkGray, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = reservation.vehicle, color = Color.DarkGray, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = spotType, color = Color.DarkGray, fontSize = 14.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ViewReservationsScreenPreview() {
    ViewReservationsScreen()
}