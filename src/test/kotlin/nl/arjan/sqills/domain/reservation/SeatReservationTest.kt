package nl.arjan.sqills.domain.reservation

import nl.arjan.sqills.domain.inventory.*
import java.time.LocalDate
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SeatReservationTest {

    private val paris = Station("Paris")
    private val brussels = Station("Brussels")
    private val amsterdam = Station("Amsterdam")
    private val berlin = Station("Berlin")

    private val firstClassSeatA11 = Seat("A", 11, SeatClass.FIRST)

    private val route = Route(
        name = "Paris to Berlin",
        stops = listOf(
            RouteStop(paris, 0),
            RouteStop(brussels, 300),
            RouteStop(amsterdam, 200),
            RouteStop(berlin, 650)
        )
    )

    private val service = TrainService(
        number = 5160,
        date = LocalDate.of(2021, 4, 1),
        route = route,
        carriages = emptyList()
    )

    @Test
    fun `same seat overlaps on same journey`() {
        val existing = reservation(paris, amsterdam)
        val requested = reservation(paris, amsterdam)

        assertTrue(existing.overlapsWith(requested))
    }

    @Test
    fun `same seat does not overlap when second journey starts where first ends`() {
        val existing = reservation(paris, amsterdam)
        val requested = reservation(amsterdam, berlin)

        assertFalse(existing.overlapsWith(requested))
    }

    @Test
    fun `same seat overlaps when requested journey is inside existing journey`() {
        val existing = reservation(paris, berlin)
        val requested = reservation(brussels, amsterdam)

        assertTrue(existing.overlapsWith(requested))
    }

    @Test
    fun `same seat does not overlap when journeys are separate`() {
        val existing = reservation(paris, brussels)
        val requested = reservation(amsterdam, berlin)

        assertFalse(existing.overlapsWith(requested))
    }

    private fun reservation(origin: Station, destination: Station): SeatReservation {
        return SeatReservation(
            service = service,
            seat = firstClassSeatA11,
            origin = origin,
            destination = destination,
            bookingId = UUID.randomUUID()
        )
    }
}