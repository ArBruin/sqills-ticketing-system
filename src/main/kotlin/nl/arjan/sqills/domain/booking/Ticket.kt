package nl.arjan.sqills.domain.booking

import nl.arjan.sqills.domain.inventory.Seat
import nl.arjan.sqills.domain.inventory.Station
import nl.arjan.sqills.domain.inventory.TrainService

data class Ticket(
    val service: TrainService,
    val origin: Station,
    val destination: Station,
    val seat: Seat
)