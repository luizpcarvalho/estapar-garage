package com.estapar.service

import com.estapar.dto.ExitRequest
import com.estapar.dto.WebhookResponse
import com.estapar.entity.Payment
import com.estapar.entity.SessionStatus
import com.estapar.repository.GarageSectorRepository
import com.estapar.repository.ParkingSessionRepository
import com.estapar.repository.PaymentRepository
import com.estapar.repository.SectorSpotRepository
import io.micronaut.http.HttpResponse
import io.micronaut.transaction.annotation.Transactional
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Singleton
open class VehicleExitService (
    private val paymentRepo: PaymentRepository,
    private val sectorRepo: GarageSectorRepository,
    private val sessionRepo: ParkingSessionRepository,
    private val spotRepo: SectorSpotRepository,
    private val pricingService: PricingService
) {

    private val logger = LoggerFactory.getLogger(VehicleExitService::class.java)

    @Transactional
    open fun processVehicleExit(exitRequest: ExitRequest): HttpResponse<WebhookResponse> {

        logger.info("Started processing vehicle exit")

        val licensePlate = exitRequest.licensePlate ?: ""

        val session = sessionRepo.findByLicensePlateAndStatus(licensePlate, SessionStatus.ACTIVE)
            ?: return HttpResponse.badRequest(WebhookResponse(error = "Vehicle is not parked"))

        val exitTime = LocalDateTime.parse(exitRequest.exitTime ?: "").toInstant(ZoneOffset.UTC)

        val basePrice = session.garageSector?.basePrice ?: BigDecimal(0)

        val amount = pricingService.calculate(session.entryTime, exitTime, basePrice, session.capacityModifier)

        val updatedGarageSector = session.garageSector?.copy(currentCapacity = session.garageSector.currentCapacity - 1)
        sectorRepo.update(updatedGarageSector)
        logger.info("Garage sector updated: $updatedGarageSector")

        val updatedSectorSpot = session.sectorSpot?.copy(occupied = false)
        spotRepo.update(updatedSectorSpot)
        logger.info("Sector spot updated: $updatedSectorSpot")

        val updatedSession = session.copy(
            exitTime = exitTime,
            status = SessionStatus.COMPLETED
        )
        sessionRepo.update(updatedSession)
        logger.info("Parking session updated: $updatedSession")

        val payment = Payment(
            session = updatedSession,
            amount = amount
        )
        paymentRepo.save(payment)
        logger.info("Payment saved $payment")

        logger.info("Finished processing vehicle exit")

        return HttpResponse.ok(WebhookResponse(data = listOf(licensePlate, exitTime, amount)))
    }

}