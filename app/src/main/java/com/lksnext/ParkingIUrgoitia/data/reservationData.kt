package com.lksnext.ParkingIUrgoitia.data
import androidx.compose.ui.graphics.vector.ImageVector
data class ParkingSpot(
    val id: String,
    val type: SpotType
)
enum class SpotType {
    STANDARD, DISABLED, ELECTRIC, MOTORBIKE
}
data class Reservation(
    val date: String,
    val startTime: String,
    val endTime: String,
    val vehicle: String,
    val spot: String
)
//Simulacion de base de datos en memoria de los estacionamientos y reservas.
object MemoryDatabase {
    //Reservas almacenadas en memoria
    val reservationsList = mutableListOf<Reservation>()
    //Estcinamientos del parking
    val allSpots = listOf(
        ParkingSpot("A1", SpotType.STANDARD),
        ParkingSpot("A2", SpotType.STANDARD),
        ParkingSpot("A3", SpotType.STANDARD),
        ParkingSpot("A4", SpotType.STANDARD),
        ParkingSpot("B1", SpotType.DISABLED),
        ParkingSpot("B2", SpotType.DISABLED),
        ParkingSpot("B3", SpotType.DISABLED),
        ParkingSpot("B4", SpotType.DISABLED),
        ParkingSpot("C1", SpotType.ELECTRIC),
        ParkingSpot("C2", SpotType.ELECTRIC),
        ParkingSpot("C3", SpotType.ELECTRIC),
        ParkingSpot("C4", SpotType.ELECTRIC),
        ParkingSpot("D1", SpotType.MOTORBIKE),
        ParkingSpot("D2", SpotType.MOTORBIKE),
        ParkingSpot("D3", SpotType.MOTORBIKE),
        ParkingSpot("D4", SpotType.MOTORBIKE)
    )
}