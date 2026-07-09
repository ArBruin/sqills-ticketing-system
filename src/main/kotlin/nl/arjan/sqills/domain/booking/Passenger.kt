package nl.arjan.sqills.domain.booking

data class Passenger(
    val name: String,
    val tickets: List<Ticket>
)