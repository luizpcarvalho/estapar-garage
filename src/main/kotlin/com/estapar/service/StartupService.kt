package com.estapar.service

import com.estapar.dto.GarageConfigResponse
import com.estapar.dto.GarageResponse
import com.estapar.dto.SpotResponse
import com.estapar.entity.GarageSector
import com.estapar.entity.SectorSpot
import com.estapar.repository.GarageSectorRepository
import com.estapar.repository.SectorSpotRepository
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
class StartupService (
    private val sectorRepo: GarageSectorRepository,
    private val spotRepo: SectorSpotRepository,
) {

    private val logger = LoggerFactory.getLogger(StartupService::class.java)

    fun saveGarageConfig(garageConfigResponse: GarageConfigResponse) {
        garageConfigResponse.garage.forEach { garageSector -> saveGarageSector(garageSector) }
        garageConfigResponse.spots.forEach { spot -> saveSectorSpot(spot)}
    }

    private fun saveGarageSector(garageSector: GarageResponse) {
        val existingGarageSector = sectorRepo.findBySector(garageSector.sector)
        if (existingGarageSector == null) {
            val newGarageSector = GarageSector(
                sector = garageSector.sector,
                basePrice = garageSector.basePrice,
                maxCapacity = garageSector.maxCapacity,
                openHour = garageSector.openHour,
                closeHour = garageSector.closeHour,
                durationLimitMinutes = garageSector.durationLimitMinutes,
            )
            sectorRepo.save(newGarageSector)
            logger.info("Garage sector saved $newGarageSector")
        }
    }

    private fun saveSectorSpot(spot: SpotResponse) {
        val existingSpot = spotRepo.findById(spot.id)
        if(existingSpot.isEmpty) {
            val newSectorSpot = SectorSpot(
                id = spot.id,
                sector = spot.sector,
                lat = spot.lat,
                lng = spot.lng,
                occupied = spot.occupied,
            )
            spotRepo.save(newSectorSpot)
            logger.info("Sector spot saved $newSectorSpot")
        }
    }

}