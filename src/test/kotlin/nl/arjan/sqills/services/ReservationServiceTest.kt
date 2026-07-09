package nl.arjan.sqills.services

import nl.arjan.sqills.api.dto.PassengerRequest
import nl.arjan.sqills.api.dto.ReservationRequest
import nl.arjan.sqills.api.dto.TicketRequest
import nl.arjan.sqills.domain.inventory.Carriage
import nl.arjan.sqills.domain.inventory.Route
import nl.arjan.sqills.domain.inventory.RouteStop
import nl.arjan.sqills.domain.inventory.Seat
import nl.arjan.sqills.domain.inventory.SeatClass
import nl.arjan.sqills.domain.inventory.Station
import nl.arjan.sqills.domain.inventory.TrainService
import nl.arjan.sqills.exceptions.SeatAlreadyReservedException
import nl.arjan.sqills.exceptions.SeatNotFoundException
import nl.arjan.sqills.exceptions.TrainServiceNotFoundException
import nl.arjan.sqills.repositories.InMemoryBookingRepository
import nl.arjan.sqills.repositories.InMemoryTrainServiceRepository
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ReservationServiceTest {

    private val bookingRepository = InMemoryBookingRepository()
    private val trainServiceRepository = InMemoryTrainServiceRepository()
    private val reservationService = ReservationService(
        bookingRepository,
        trainServiceRepository
    )

    @Test
    fun `can reserve two first class seats`() {
        // Arrange
        givenParisAmsterdamService()
        val request = createParisAmsterdamRequest()

        // Act
        val booking = reservationService.reserve(request)

        // Assert
        assertEquals(2, booking.passengers.size)
        assertEquals(1, bookingRepository.findAll().size)
        assertEquals(1, booking.passengers[0].tickets.size)
        assertEquals(1, booking.passengers[1].tickets.size)

        val firstTicket = booking.passengers[0].tickets[0]
        val secondTicket = booking.passengers[1].tickets[0]

        assertEquals("A", firstTicket.seatReservation.seat.carriage)
        assertEquals(11, firstTicket.seatReservation.seat.number)

        assertEquals("A", secondTicket.seatReservation.seat.carriage)
        assertEquals(12, secondTicket.seatReservation.seat.number)

        assertEquals(SeatClass.FIRST, firstTicket.seatReservation.seat.seatClass)
        assertEquals(SeatClass.FIRST, secondTicket.seatReservation.seat.seatClass)
    }

    @Test
    fun `cannot reserve an already reserved seat`() {
        // Arrange
        givenParisAmsterdamService()
        val request = createParisAmsterdamRequest()

        // Act
        reservationService.reserve(request)

        // Assert
        assertFailsWith<SeatAlreadyReservedException> {
            reservationService.reserve(request)
        }
    }

    @Test
    fun `can reserve seats with different classes and seat changes`() {
        // Arrange
        givenLondonAmsterdamService()
        val request = createLondonAmsterdamRequest()

        // Act
        val booking = reservationService.reserve(request)

        // Assert
        assertEquals(2, booking.passengers.size)
        assertEquals(2, booking.passengers[0].tickets.size)
        assertEquals(2, booking.passengers[1].tickets.size)
    }

    @Test
    fun `cannot reserve same seats with seat changes twice`() {
        // Arrange
        givenLondonAmsterdamService()
        val request = createLondonAmsterdamRequest()

        // Act
        reservationService.reserve(request)

        // Assert
        assertFailsWith<SeatAlreadyReservedException> {
            reservationService.reserve(request)
        }
    }

    @Test
    fun `cannot reserve when train service does not exist`() {
        // Arrange
        val request = createParisAmsterdamRequest()

        // Act & Assert
        assertFailsWith<TrainServiceNotFoundException> {
            reservationService.reserve(request)
        }
    }

    @Test
    fun `cannot reserve when seat does not exist`() {
        // Arrange
        givenParisAmsterdamService()

        val request = ReservationRequest(
            serviceNumber = 5160,
            serviceDate = LocalDate.of(2021, 4, 1),
            passengers = listOf(
                PassengerRequest(
                    name = "Passenger 1",
                    tickets = listOf(
                        TicketRequest("Paris", "Amsterdam", "Z", 99)
                    )
                )
            )
        )

        // Act & Assert
        assertFailsWith<SeatNotFoundException> {
            reservationService.reserve(request)
        }
    }

    private fun givenParisAmsterdamService(): TrainService {
        val paris = Station("Paris")
        val brussels = Station("Brussels")
        val amsterdam = Station("Amsterdam")

        val route = Route(
            name = "Paris to Amsterdam",
            stops = listOf(
                RouteStop(paris, 0),
                RouteStop(brussels, 300),
                RouteStop(amsterdam, 200)
            )
        )

        val carriageA = Carriage(
            code = "A",
            seats = listOf(
                Seat("A", 11, SeatClass.FIRST),
                Seat("A", 12, SeatClass.FIRST)
            )
        )

        val service = TrainService(
            number = 5160,
            date = LocalDate.of(2021, 4, 1),
            route = route,
            carriages = listOf(carriageA)
        )

        trainServiceRepository.save(service)

        return service
    }

    private fun givenLondonAmsterdamService(): TrainService {
        val london = Station("London")
        val paris = Station("Paris")
        val amsterdam = Station("Amsterdam")

        val route = Route(
            name = "London to Amsterdam",
            stops = listOf(
                RouteStop(london, 0),
                RouteStop(paris, 300),
                RouteStop(amsterdam, 200)
            )
        )

        val carriageA = Carriage(
            code = "A",
            seats = listOf(
                Seat("A", 1, SeatClass.SECOND)
            )
        )

        val carriageH = Carriage(
            code = "H",
            seats = listOf(
                Seat("H", 1, SeatClass.SECOND)
            )
        )

        val carriageN = Carriage(
            code = "N",
            seats = listOf(
                Seat("N", 5, SeatClass.FIRST)
            )
        )

        val carriageT = Carriage(
            code = "T",
            seats = listOf(
                Seat("T", 7, SeatClass.FIRST)
            )
        )

        val service = TrainService(
            number = 5161,
            date = LocalDate.of(2021, 4, 1),
            route = route,
            carriages = listOf(carriageA, carriageH, carriageN, carriageT)
        )

        trainServiceRepository.save(service)

        return service
    }

    private fun createParisAmsterdamRequest(): ReservationRequest {
        return ReservationRequest(
            serviceNumber = 5160,
            serviceDate = LocalDate.of(2021, 4, 1),
            passengers = listOf(
                PassengerRequest(
                    name = "Passenger 1",
                    tickets = listOf(
                        TicketRequest("Paris", "Amsterdam", "A", 11)
                    )
                ),
                PassengerRequest(
                    name = "Passenger 2",
                    tickets = listOf(
                        TicketRequest("Paris", "Amsterdam", "A", 12)
                    )
                )
            )
        )
    }

    private fun createLondonAmsterdamRequest(): ReservationRequest {
        return ReservationRequest(
            serviceNumber = 5161,
            serviceDate = LocalDate.of(2021, 4, 1),
            passengers = listOf(
                PassengerRequest(
                    name = "Passenger 1",
                    tickets = listOf(
                        TicketRequest("London", "Paris", "H", 1),
                        TicketRequest("Paris", "Amsterdam", "A", 1)
                    )
                ),
                PassengerRequest(
                    name = "Passenger 2",
                    tickets = listOf(
                        TicketRequest("London", "Paris", "N", 5),
                        TicketRequest("Paris", "Amsterdam", "T", 7)
                    )
                )
            )
        )
    }
}