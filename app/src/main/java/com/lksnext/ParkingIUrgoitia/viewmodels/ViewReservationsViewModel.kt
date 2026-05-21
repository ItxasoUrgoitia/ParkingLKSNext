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

// El estado que la pantalla leerá
data class ViewReservationsUiState(
    val selectedTabIndex: Int = 0,
    val activeReservations: List<Reservation> = emptyList(),
    val historyReservations: List<Reservation> = emptyList()
)

class ViewReservationsViewModel(
    private val repository: ReservationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ViewReservationsUiState())
    val uiState: StateFlow<ViewReservationsUiState> = _uiState.asStateFlow()

    init {
        loadReservations()
    }

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
    }

    // Aislamos la lógica de negocio (filtrado de fechas) en el ViewModel
    fun loadReservations() {
        val allReservations = repository.getAllReservations()

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        val active = mutableListOf<Reservation>()
        val history = mutableListOf<Reservation>()

        allReservations.forEach { res ->
            try {
                val resDate = dateFormat.parse(res.date)
                if (resDate != null && resDate.before(today)) {
                    history.add(res)
                } else {
                    active.add(res)
                }
            } catch (e: Exception) {
                // Si la fecha falla, por seguridad la consideramos activa
                active.add(res)
            }
        }

        _uiState.update {
            it.copy(
                activeReservations = active,
                historyReservations = history
            )
        }
    }
}