package com.lksnext.ParkingIUrgoitia.data
//Interfaz del repositorio de reservas, define las operaciones que se pueden realizar con las reservas y los estacionamientos.
import java.text.SimpleDateFormat
import java.util.Locale
interface ReservationRepository {
    fun getParkingSpots(): List<ParkingSpot>
    fun addReservation(reservation: Reservation)
    fun isSpotOccupied(spot: String, date: String, startTime: String, endTime: String): Boolean
}

//Implementación del repositorio de reservas que utiliza una base de datos en memoria para almacenar las reservas y los estacionamientos.
class ReservationRepositoryImpl : ReservationRepository {
    override fun getParkingSpots(): List<ParkingSpot> = MemoryDatabase.allSpots

    override fun addReservation(reservation: Reservation) {
        MemoryDatabase.reservationsList.add(reservation)
    }

    override fun isSpotOccupied(spot: String, date: String, startTime: String, endTime: String): Boolean {
        if (startTime == "--:--" || endTime == "--:--") {
            return false
        }

        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        return try {
            val newStart = format.parse(startTime)?.time ?: return false
            var newEnd = format.parse(endTime)?.time ?: return false


            if (newEnd <= newStart) newEnd += 24 * 60 * 60 * 1000L

            MemoryDatabase.reservationsList.any { existingRes ->
                if (existingRes.spot == spot && existingRes.date == date) {
                    val existingStart = format.parse(existingRes.startTime)?.time ?: 0L
                    var existingEnd = format.parse(existingRes.endTime)?.time ?: 0L

                    if (existingEnd <= existingStart) existingEnd += 24 * 60 * 60 * 1000L
                    (newStart < existingEnd) && (newEnd > existingStart)
                } else {
                    false
                }
            }
        } catch (e: Exception) {
            false
        }
    }
}