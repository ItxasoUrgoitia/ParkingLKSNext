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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lksnext.ParkingIUrgoitia.data.Reservation
import com.lksnext.ParkingIUrgoitia.viewmodels.ViewReservationsViewModel
import java.text.SimpleDateFormat
import java.util.Locale

private val lksOrange = Color(0xFFF25C05)

@Composable
fun ViewReservationsScreen(
    viewModel: ViewReservationsViewModel,
    onNavigateHome: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    // Al entrar a la pantalla, se recarga la lista por si hubo reservas nuevas
    LaunchedEffect(Unit) {
        viewModel.loadReservations()
    }

    val displayedReservations = if (uiState.selectedTabIndex == 0) {
        uiState.activeReservations
    } else {
        uiState.historyReservations
    }

    Scaffold(
        topBar = { ViewReservationsTopBar(onNavigateHome) },
        containerColor = Color(0xFFFAFAFA)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ReservationsTabRow(
                selectedTabIndex = uiState.selectedTabIndex,
                onTabSelected = { viewModel.onTabSelected(it) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (displayedReservations.isEmpty()) {
                EmptyReservationsMessage()
            } else {
                ReservationsList(
                    reservations = displayedReservations,
                    isHistory = uiState.selectedTabIndex == 1
                )
            }
        }
    }
}


// Cada uno de estos componentes se encarga de una parte específica de la UI, lo que hace que el código sea más modular y fácil de entender.
@Composable
private fun ViewReservationsTopBar(onNavigateHome: () -> Unit) {
    Surface(shadowElevation = 3.dp, color = Color.White, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateHome) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("View Reservations", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onNavigateHome) { Icon(Icons.Default.Home, "Home", tint = lksOrange) }
            IconButton(onClick = { }) { Icon(Icons.Default.Person, "Profile", tint = lksOrange) }
        }
    }
}

@Composable
private fun ReservationsTabRow(selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf("Active", "History")

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
                onClick = { onTabSelected(index) },
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
}

@Composable
private fun EmptyReservationsMessage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "No reservations found.", color = Color.Gray, fontSize = 16.sp)
    }
}

@Composable
private fun ReservationsList(reservations: List<Reservation>, isHistory: Boolean) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(reservations) { reservation ->
            ReservationCard(reservation = reservation, isHistory = isHistory)
        }
        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

@Composable
private fun ReservationCard(reservation: Reservation, isHistory: Boolean) {
    val pillBgColor = if (isHistory) Color(0xFFE5E7EB) else lksOrange
    val pillTextColor = if (isHistory) Color.DarkGray else Color.White
    val pillText = if (isHistory) "completed" else "upcoming"

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
                    fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black
                )
                Surface(shape = RoundedCornerShape(16.dp), color = pillBgColor) {
                    Text(
                        text = pillText, color = pillTextColor, fontSize = 12.sp,
                        fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFF3F4F6), thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))

            // Sub-componente para no repetir la estructura de Icono + Texto
            ReservationDetailRow(icon = Icons.Default.CalendarToday, text = formatReservationDate(reservation.date))
            ReservationDetailRow(icon = Icons.Default.Schedule, text = "${reservation.startTime} - ${reservation.endTime}")
            ReservationDetailRow(icon = Icons.Default.DirectionsCar, text = reservation.vehicle)
            ReservationDetailRow(icon = Icons.Default.LocationOn, text = getSpotTypeName(reservation.spot))
        }
    }
}

@Composable
private fun ReservationDetailRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 12.dp)) {
        Icon(icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, color = Color.DarkGray, fontSize = 14.sp)
    }
}


//Funciones auxiliares para formatear la fecha y el tipo de plaza, para que se vea más amigable al usuario.
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

private fun getSpotTypeName(spot: String): String {
    return when (spot.firstOrNull()) {
        'B' -> "disabled"
        'C' -> "electric"
        'D' -> "motorbike"
        else -> "standard"
    }
}