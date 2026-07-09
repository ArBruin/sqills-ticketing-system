package nl.arjan.sqills.domain.inventory

data class Route(
    val name: String,
    val stops: List<RouteStop>
) {
    fun isValidJourney(origin: Station, destination: Station): Boolean {
        val originIndex = stops.indexOfFirst { it.station == origin }

        if (originIndex == -1) {
            return false
        }

        return stops
            .drop(originIndex + 1)
            .any { it.station == destination }
    }

    fun indexOfStation(station: Station): Int {
        return stops.indexOfFirst { it.station == station }
    }
}