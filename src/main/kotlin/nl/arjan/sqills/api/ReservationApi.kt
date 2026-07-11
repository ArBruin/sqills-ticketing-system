package nl.arjan.sqills.api

import nl.arjan.sqills.api.dto.ReservationRequest

interface ReservationApi {
    fun reserve(request: ReservationRequest): Response
}