package com.lksnext.ParkingIUrgoitia.viewmodels

import androidx.lifecycle.ViewModel
import com.lksnext.ParkingIUrgoitia.data.Reservation
import com.lksnext.ParkingIUrgoitia.data.ReservationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// El estado que observará la pantalla
data class DeleteReservationUiState(
    val upcomingReservations: List<Reservation> = emptyList()
)

class DeleteReservationViewModel(
    private val repository: ReservationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DeleteReservationUiState())
    val uiState: StateFlow<DeleteReservationUiState> = _uiState.asStateFlow()

    init {
        loadUpcomingReservations()
    }

    // Filtra y carga solo las reservas futuras
    fun loadUpcomingReservations() {
        val allReservations = repository.getAllReservations()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }.time

        val upcoming = allReservations.filter { res ->
            try {
                val resDate = dateFormat.parse(res.date)
                resDate != null && !resDate.before(today)
            } catch (e: Exception) {
                false
            }
        }

        _uiState.update { it.copy(upcomingReservations = upcoming) }
    }

    // Orden de borrado: Borra en el repositorio y recarga la lista
    fun deleteReservation(reservation: Reservation) {
        repository.deleteReservation(reservation)
        loadUpcomingReservations()
    }
}