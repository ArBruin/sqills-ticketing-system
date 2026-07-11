package nl.arjan.sqills.domain.inventory

import java.time.LocalDate

data class TrainServiceId(
    val number: Int,
    val date: LocalDate
)

data class TrainService(
    val id: TrainServiceId,
    val route: Route,
    val carriages: List<Carriage>
)