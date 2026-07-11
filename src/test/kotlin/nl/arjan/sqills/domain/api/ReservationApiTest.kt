package nl.arjan.sqills.api

import nl.arjan.sqills.api.dto.ReservationRequest
import nl.arjan.sqills.repositories.InMemoryBookingRepository
import nl.arjan.sqills.repositories.InMemoryTrainServiceRepository
import nl.arjan.sqills.services.ReservationService
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class ReservationApiTest {

    @Test
    fun `returns 404 when train service does not exist`() {
        // Arrange
        val reservationService = ReservationService(
            bookingRepository = InMemoryBookingRepository(),
            trainServiceRepository = InMemoryTrainServiceRepository()
        )

        val api = ReservationApiImpl(reservationService)

        val request = ReservationRequest(
            serviceNumber = 9999,
            serviceDate = LocalDate.of(2021, 4, 1),
            passengers = emptyList()
        )

        // Act
        val response = api.reserve(request)

        // Assert
        assertEquals(404, response.getStatusCode())
    }
}