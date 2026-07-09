package nl.arjan.sqills.api.dto

data class PassengerRequest(
    val name: String,
    val tickets: List<TicketRequest>
)