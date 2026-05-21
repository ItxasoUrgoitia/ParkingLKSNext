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
import com.lksnext.ParkingIUrgoitia.data.SpotType
// ViewModel para la pantalla de reserva.

//Clase de datos que agrupa lo que la pantalla necesita saber para dibujarse.
data class ReservationUiState(
    val date: String = "dd/mm/aaaa",
    val spots: List<ParkingSpotState> = emptyList(),
    val startTime: String = "--:--",
    val endTime: String = "--:--",
    val selectedVehicle: String = "",
    val selectedSpot: String = "",
    val errorMessage: String = "",
    val isSuccess: Boolean = false
)
data class ParkingSpotState(
    val id: String,
    val type: SpotType,
    val isOccupied: Boolean
)
class ReservationViewModel(
    private val repository: ReservationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReservationUiState())
    val uiState: StateFlow<ReservationUiState> = _uiState.asStateFlow()

    init {
        loadSpots()
    }

    private fun loadSpots() {
        //Llamamos al repositorio para obtener los estacionamientos y verificamos si están ocupados para la fecha seleccionada.
        val state = _uiState.value
        val spots = repository.getParkingSpots().map { spot ->
            ParkingSpotState(
                id = spot.id,
                type = spot.type,
                isOccupied = repository.isSpotOccupied(
                    spot = spot.id,
                    date = state.date,
                    startTime = state.startTime,
                    endTime = state.endTime
                )
            )
        }
        val isSelectedSpotNowOccupied = spots.find { it.id == state.selectedSpot }?.isOccupied == true
        _uiState.update { it.copy(
            spots = spots,
            selectedSpot = if (isSelectedSpotNowOccupied) "" else it.selectedSpot
        ) }
    }
    //Cada vez que el usuario selecciona una fecha, hora o vehículo, actualizamos el estado y recargamos los estacionamientos para verificar su disponibilidad.
    fun onDateSelected(date: String) {
        _uiState.update { it.copy(date = date, errorMessage = "") }
        loadSpots()
    }
    fun onStartTimeSelected(time: String) {
        _uiState.update { it.copy(startTime = time, errorMessage = "") }
        loadSpots()
    }
    fun onEndTimeSelected(time: String) {
        _uiState.update{ it.copy(endTime = time, errorMessage = "") }
        loadSpots()
    }
    fun onVehicleSelected(vehicle: String) = _uiState.update { it.copy(selectedVehicle = vehicle, errorMessage = "") }

    fun onSpotSelected(spot: String) {
        val currentState = _uiState.value

        if (!repository.isSpotOccupied(spot, currentState.date, currentState.startTime, currentState.endTime)) {
            _uiState.update { it.copy(selectedSpot = spot, errorMessage = "") }
        }
    }


    fun validateAndSaveReservation() {
        val state = _uiState.value

        if (state.date == "dd/mm/aaaa" || state.startTime == "--:--" || state.endTime == "--:--" ||
            state.selectedVehicle.isEmpty() || state.selectedSpot.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Please fill all fields and select a parking spot.") }
            return
        }

        try {
            val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val selectedDate = dateFormatter.parse(state.date)

            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
            }
            val today = calendar.time
            calendar.add(Calendar.DAY_OF_YEAR, 7)
            val maxDate = calendar.time

            if (selectedDate != null && selectedDate.before(today)) {
                _uiState.update { it.copy(errorMessage = "Reservation date cannot be in the past.") }
                return
            } else if (selectedDate != null && selectedDate.after(maxDate)) {
                _uiState.update { it.copy(errorMessage = "Reservations must be within 7 days from today.") }
                return
            }

            val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            val start = timeFormatter.parse(state.startTime)
            val end = timeFormatter.parse(state.endTime)

            if (start != null && end != null) {
                var diff = end.time - start.time
                if (diff < 0) diff += 24 * 60 * 60 * 1000

                val durationInHours = diff / (1000.0 * 60.0 * 60.0)

                if (durationInHours > 9.0) {
                    _uiState.update { it.copy(errorMessage = "Maximum duration is 9 hours.") }
                } else if (durationInHours == 0.0) {
                    _uiState.update { it.copy(errorMessage = "Start and end times cannot be the same.") }
                } else {

                    val newReservation = Reservation(
                        date = state.date,
                        startTime = state.startTime,
                        endTime = state.endTime,
                        vehicle = state.selectedVehicle,
                        spot = state.selectedSpot
                    )
                    repository.addReservation(newReservation)
                    _uiState.update { it.copy(errorMessage = "", isSuccess = true) }
                }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = "Error validating date or time format.") }
        }
    }
}