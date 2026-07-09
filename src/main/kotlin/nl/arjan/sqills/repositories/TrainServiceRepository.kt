package nl.arjan.sqills.repositories

import nl.arjan.sqills.domain.inventory.TrainService
import java.time.LocalDate

interface TrainServiceRepository {

    fun save(trainService: TrainService)

    fun findByNumberAndDate(
        number: Int,
        date: LocalDate
    ): TrainService?
}