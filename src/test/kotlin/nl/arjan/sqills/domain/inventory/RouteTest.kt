package nl.arjan.sqills.domain.inventory

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RouteTest {

    private val paris = Station("Paris")
    private val brussels = Station("Brussels")
    private val amsterdam = Station("Amsterdam")

    private val route = Route(
        name = "Paris to Amsterdam",
        stops = listOf(
            RouteStop(paris, 0),
            RouteStop(brussels, 300),
            RouteStop(amsterdam, 200)
        )
    )

    @Test
    fun `journey is valid when origin is before destination`() {
        assertTrue(route.containsJourney(paris, amsterdam))
    }

    @Test
    fun `journey is invalid when origin is after destination`() {
        assertFalse(route.containsJourney(amsterdam, paris))
    }

    @Test
    fun `journey is invalid when origin does not exist`() {
        assertFalse(route.containsJourney(Station("London"), amsterdam))
    }

    @Test
    fun `journey is invalid when destination does not exist`() {
        assertFalse(route.containsJourney(paris, Station("London")))
    }
}