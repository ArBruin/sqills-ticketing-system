package nl.arjan.sqills.repositories

import nl.arjan.sqills.domain.booking.Booking
import nl.arjan.sqills.domain.reservation.SeatReservation

class InMemoryBookingRepository : BookingRepository {

    private val bookings = mutableListOf<Booking>()

    override fun save(booking: Booking) {
        bookings.add(booking)
    }

    override fun findAll(): List<Booking> {
        return bookings.toList()
    }

    override fun findSeatReservations(): List<SeatReservation> {
        return bookings
            .flatMap { it.passengers }
            .flatMap { it.tickets }
            .map { it.seatReservation }
    }
}