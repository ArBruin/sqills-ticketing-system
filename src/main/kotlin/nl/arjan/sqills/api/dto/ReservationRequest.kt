package nl.arjan.sqills.api.dto

import java.time.LocalDate

data class ReservationRequest(
    val serviceNumber: Int,
    val serviceDate: LocalDate,
    val passengers: List<PassengerRequest>
)