package nl.arjan.sqills.domain.booking

import nl.arjan.sqills.domain.reservation.SeatReservation

data class Ticket(
    val seatReservation: SeatReservation
)