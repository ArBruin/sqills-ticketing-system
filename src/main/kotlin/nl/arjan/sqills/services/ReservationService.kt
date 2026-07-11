package nl.arjan.sqills.services

import nl.arjan.sqills.api.dto.ReservationRequest
import nl.arjan.sqills.domain.booking.Booking
import nl.arjan.sqills.domain.booking.Passenger
import nl.arjan.sqills.domain.booking.Ticket
import nl.arjan.sqills.domain.inventory.Station
import nl.arjan.sqills.domain.reservation.SeatReservation
import nl.arjan.sqills.exceptions.InvalidJourneyException
import nl.arjan.sqills.exceptions.SeatAlreadyReservedException
import nl.arjan.sqills.exceptions.SeatNotFoundException
import nl.arjan.sqills.exceptions.TrainServiceNotFoundException
import nl.arjan.sqills.repositories.BookingRepository
import nl.arjan.sqills.repositories.TrainServiceRepository
import java.util.UUID

class ReservationService(
    private val bookingRepository: BookingRepository,
    private val trainServiceRepository: TrainServiceRepository
) {

    fun reserve(request: ReservationRequest): Booking {
        val trainService = trainServiceRepository.findByNumberAndDate(
            request.serviceNumber,
            request.serviceDate
        ) ?: throw TrainServiceNotFoundException("Train service not found")

        val bookingId = UUID.randomUUID()

        val passengers = request.passengers.map { passengerRequest ->
            Passenger(
                name = passengerRequest.name,
                tickets = passengerRequest.tickets.map { ticketRequest ->
                    val origin = Station(ticketRequest.origin)
                    val destination = Station(ticketRequest.destination)

                    if (!trainService.route.isValidJourney(origin, destination)) {
                        throw InvalidJourneyException(
                            "Journey ${ticketRequest.origin} to ${ticketRequest.destination} " +
                                    "is not valid for service ${trainService.id.number}."
                        )
                    }

                    Ticket(
                        seatReservation = SeatReservation(
                            service = trainService,
                            seat = trainService.carriages
                                .flatMap { it.seats }
                                .firstOrNull {
                                    it.carriage == ticketRequest.carriage &&
                                            it.number == ticketRequest.seatNumber
                                } ?: throw SeatNotFoundException("Seat not found"),
                            origin = Station(ticketRequest.origin),
                            destination = Station(ticketRequest.destination),
                            bookingId = bookingId
                        )
                    )
                }
            )
        }

        val booking = Booking(
            id = bookingId,
            passengers = passengers
        )

        val existingReservations = bookingRepository.findSeatReservations()
        val requestedReservations = passengers
            .flatMap { it.tickets }
            .map { it.seatReservation }

        requestedReservations.forEachIndexed { index, requestedReservation ->
            val conflictsWithinRequest = requestedReservations
                .drop(index + 1)
                .any { otherReservation ->
                    requestedReservation.overlapsWith(otherReservation)
                }

            if (conflictsWithinRequest) {
                throw SeatAlreadyReservedException(
                    "Seat ${requestedReservation.seat.carriage}" +
                            "${requestedReservation.seat.number} is reserved more than once in the same request."
                )
            }
        }

        requestedReservations.forEach { requestedReservation ->
            val alreadyReserved = existingReservations.any { existingReservation ->
                existingReservation.overlapsWith(requestedReservation)
            }

            if (alreadyReserved) {
                throw SeatAlreadyReservedException(
                    "Seat ${requestedReservation.seat.carriage}${requestedReservation.seat.number} is already reserved."
                )
            }
        }

        bookingRepository.save(booking)

        return booking
    }
}