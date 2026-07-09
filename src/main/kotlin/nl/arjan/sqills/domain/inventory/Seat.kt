package nl.arjan.sqills.domain.inventory

data class Seat(
    val carriage: String,
    val number: Int,
    val seatClass: SeatClass
)