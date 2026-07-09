package nl.arjan.sqills.repositories

import nl.arjan.sqills.domain.booking.Booking
import nl.arjan.sqills.domain.reservation.SeatReservation

interface BookingRepository {

    fun save(booking: Booking)

    fun findAll(): List<Booking>

    fun findSeatReservations(): List<SeatReservation>
}