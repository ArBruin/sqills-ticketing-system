package nl.arjan.sqills.domain.reservation

import nl.arjan.sqills.domain.inventory.Seat
import nl.arjan.sqills.domain.inventory.Station
import nl.arjan.sqills.domain.inventory.TrainService
import java.util.UUID

data class SeatReservation(
    val service: TrainService,
    val seat: Seat,
    val origin: Station,
    val destination: Station,
    val bookingId: UUID
) {
    fun overlapsWith(other: SeatReservation): Boolean {
        if (service.id != other.service.id) {
            return false
        }

        if (seat != other.seat) {
            return false
        }

        val route = service.route

        val thisOriginIndex = route.indexOfStation(origin)
        val thisDestinationIndex = route.indexOfStation(destination)
        val otherOriginIndex = route.indexOfStation(other.origin)
        val otherDestinationIndex = route.indexOfStation(other.destination)

        return thisOriginIndex < otherDestinationIndex &&
                otherOriginIndex < thisDestinationIndex
    }
}