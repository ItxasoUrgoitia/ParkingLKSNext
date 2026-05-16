package com.lksnext.ParkingIUrgoitia.data
data class Reservation(
    val date: String,
    val startTime: String,
    val endTime: String,
    val vehicle: String,
    val spot: String
)

object MemoryDatabase {
    val reservationsList = mutableListOf<Reservation>()
}