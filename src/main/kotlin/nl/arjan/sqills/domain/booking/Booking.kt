package nl.arjan.sqills.domain.booking

import java.util.UUID

data class Booking(
    val id: UUID,
    val passengers: List<Passenger>
)