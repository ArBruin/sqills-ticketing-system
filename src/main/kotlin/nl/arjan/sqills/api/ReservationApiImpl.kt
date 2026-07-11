package nl.arjan.sqills.api

import nl.arjan.sqills.api.dto.ReservationRequest
import nl.arjan.sqills.exceptions.InvalidJourneyException
import nl.arjan.sqills.exceptions.SeatAlreadyReservedException
import nl.arjan.sqills.exceptions.SeatNotFoundException
import nl.arjan.sqills.exceptions.TrainServiceNotFoundException
import nl.arjan.sqills.services.ReservationService

class ReservationApiImpl(
    private val reservationService: ReservationService
) : ReservationApi {

    override fun reserve(request: ReservationRequest): Response {
        return try {
            val booking = reservationService.reserve(request)

            SimpleResponse(
                statusCode = 201,
                body = booking
            )
        } catch (exception: TrainServiceNotFoundException) {
            errorResponse(404, exception)
        } catch (exception: SeatNotFoundException) {
            errorResponse(404, exception)
        } catch (exception: InvalidJourneyException) {
            errorResponse(400, exception)
        } catch (exception: SeatAlreadyReservedException) {
            errorResponse(409, exception)
        }
    }

    private fun errorResponse(
        statusCode: Int,
        exception: RuntimeException
    ): Response {
        return SimpleResponse(
            statusCode = statusCode,
            body = mapOf(
                "error" to (exception.message ?: "Unknown error")
            )
        )
    }
}