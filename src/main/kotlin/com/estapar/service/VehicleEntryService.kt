package com.estapar.service

import com.estapar.dto.EntryRequest
import com.estapar.dto.EntryResponse
import com.estapar.dto.WebhookResponse
import com.estapar.entity.ParkingSession
import com.estapar.entity.SessionStatus
import com.estapar.repository.ParkingSessionRepository
import io.micronaut.http.HttpResponse
import io.micronaut.transaction.annotation.Transactional
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.time.Instant

@Singleton
open class VehicleEntryService (private val sessionRepo: ParkingSessionRepository) {

    private val logger = LoggerFactory.getLogger(VehicleEntryService::class.java)

    @Transactional
    open fun processVehicleEntry(request: EntryRequest): HttpResponse<WebhookResponse> {

        logger.info("Started processing vehicle entry")

        val licensePlate = request.licensePlate ?: ""

        val existingSession = sessionRepo.findByLicensePlateAndStatus(licensePlate, SessionStatus.ACTIVE)

        if (existingSession != null) {
            logger.warn("Found existing session for license plate: $licensePlate")
            return HttpResponse.badRequest(WebhookResponse(error = "Vehicle already inside parking lot"))
        }

        val session = ParkingSession(
            licensePlate = licensePlate,
            entryTime = Instant.now(),
            status = SessionStatus.ACTIVE
        )

        logger.info("Saving parking session: $session")
        sessionRepo.save(session)

        logger.info("Finished processing vehicle entry")

        return HttpResponse.ok(WebhookResponse(data = listOf(EntryResponse(licensePlate, session.entryTime))))
    }

}