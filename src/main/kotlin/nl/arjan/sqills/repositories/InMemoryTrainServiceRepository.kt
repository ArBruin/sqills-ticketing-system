package nl.arjan.sqills.repositories

import nl.arjan.sqills.domain.inventory.TrainService
import nl.arjan.sqills.domain.inventory.TrainServiceId
import java.time.LocalDate

class InMemoryTrainServiceRepository : TrainServiceRepository {

    private val trainServices = mutableListOf<TrainService>()

    override fun save(trainService: TrainService) {
        trainServices.add(trainService)
    }

    override fun findByNumberAndDate(
        number: Int,
        date: LocalDate
    ): TrainService? {
        return trainServices.firstOrNull {
            it.id == TrainServiceId(number, date)
        }
    }
}