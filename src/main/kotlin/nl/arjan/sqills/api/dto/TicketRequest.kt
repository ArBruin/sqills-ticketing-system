package nl.arjan.sqills.api.dto

data class TicketRequest(
    val origin: String,
    val destination: String,
    val carriage: String,
    val seatNumber: Int
)