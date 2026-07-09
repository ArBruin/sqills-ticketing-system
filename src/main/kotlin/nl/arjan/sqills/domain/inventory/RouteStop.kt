package nl.arjan.sqills.domain.inventory

data class RouteStop(
    val station: Station,
    val distanceFromPrevious: Int
)