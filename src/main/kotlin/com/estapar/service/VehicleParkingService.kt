package com.estapar.service

import com.estapar.dto.ParkingRequest
import com.estapar.dto.ParkingResponse
import com.estapar.dto.WebhookResponse
import com.estapar.entity.SessionStatus
import com.estapar.repository.GarageSectorRepository
import com.estapar.repository.ParkingSessionRepository
import com.estapar.repository.SectorSpotRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.transaction.annotation.Transactional
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
open class VehicleParkingService (
    private val sectorRepo: GarageSectorRepository,
    private val sessionRepo: ParkingSessionRepository,
    private val spotRepo: SectorSpotRepository
) {

    private val logger = LoggerFactory.getLogger(VehicleParkingService::class.java)

    @Transactional
    open fun processVehicleParking(request: ParkingRequest): HttpResponse<WebhookResponse> {

        logger.info("Started processing vehicle parking")

        val licensePlate = request.licensePlate ?: ""

        val session = sessionRepo.findByLicensePlateAndStatus(licensePlate, SessionStatus.ACTIVE)
            ?: return HttpResponse.badRequest(WebhookResponse(error = "There is no parking session for the vehicle"))

        val sectorSpot = spotRepo.findByLatAndLng(request.lat, request.lng)
            ?: return HttpResponse.badRequest(WebhookResponse(error = "Sector spot not found"))

        val garageSector = sectorRepo.findBySector(sectorSpot.sector)
            ?: return HttpResponse.badRequest(WebhookResponse(error = "Garage sector not found"))

        val maxCapacity = garageSector.maxCapacity ?: garageSector.currentOccupation

        if (maxCapacity == garageSector.currentOccupation) {
            val updatedSession = session.copy(status = SessionStatus.CANCELLED)
            sessionRepo.update(updatedSession)
            logger.warn("No vacancy available. Parking session cancelled")
            return HttpResponse.status<WebhookResponse>(HttpStatus.UNPROCESSABLE_ENTITY).body(WebhookResponse(error = "Garage sector is full"))
        }

        if(sectorSpot.occupied) {
            val updatedSession = session.copy(status = SessionStatus.CANCELLED)
            sessionRepo.update(updatedSession)
            logger.warn("Sector spot unavailable. Parking session cancelled")
            return HttpResponse.status<WebhookResponse>(HttpStatus.UNPROCESSABLE_ENTITY).body(WebhookResponse(error = "Sector spot is occupied"))
        }

        val updatedSectorSpot = sectorSpot.copy(occupied = true)
        spotRepo.update(updatedSectorSpot)
        logger.info("Sector spot updated: $updatedSectorSpot")

        val updatedGarageSector = garageSector.copy(currentOccupation = garageSector.currentOccupation + 1)
        sectorRepo.update(updatedGarageSector)
        logger.info("Garage sector updated: $updatedGarageSector")

        val updatedSession = session.copy(
            garageSector = updatedGarageSector,
            sectorSpot = updatedSectorSpot,
            capacityModifier = calculateCapacityModifier(maxCapacity, garageSector.currentOccupation)
        )
        sessionRepo.update(updatedSession)
        logger.info("Parking session updated: $updatedSession")

        logger.info("Finished processing vehicle parking")

        return HttpResponse.ok(
            WebhookResponse(data = listOf(ParkingResponse(licensePlate, garageSector.sector, session.entryTime)))
        )

    }

    private fun calculateCapacityModifier(maxCapacity: Int, currentCapacity: Int): Int? {
        return if((maxCapacity * 0.25) > currentCapacity) { 10 }
        else if ((maxCapacity * 0.5) > currentCapacity) { null }
        else if ((maxCapacity * 0.75) > currentCapacity) { 110 }
        else { 125 }
    }

}