package nl.arjan.sqills.domain.inventory

data class Carriage(
    val code: String,
    val seats: List<Seat>
)