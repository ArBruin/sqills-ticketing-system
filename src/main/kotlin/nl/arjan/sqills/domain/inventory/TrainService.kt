package nl.arjan.sqills.domain.inventory

import java.time.LocalDate

data class TrainService(
    val number: Int,
    val date: LocalDate,
    val route: Route,
    val carriages: List<Carriage>
)