package com.estapar.service

import com.estapar.dto.ExitRequest
import com.estapar.dto.ExitResponse
import com.estapar.dto.WebhookResponse
import com.estapar.entity.GarageSector
import com.estapar.entity.ParkingSession
import com.estapar.entity.SectorSpot
import com.estapar.entity.SessionStatus
import com.estapar.repository.GarageSectorRepository
import com.estapar.repository.ParkingSessionRepository
import com.estapar.repository.PaymentRepository
import com.estapar.repository.SectorSpotRepository
import io.micronaut.http.HttpStatus
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneOffset

class VehicleExitServiceTest {

    private val paymentRepo = mockk<PaymentRepository>()
    private val sectorRepo = mockk<GarageSectorRepository>()
    private val sessionRepo= mockk<ParkingSessionRepository>()
    private val spotRepo = mockk<SectorSpotRepository>()
    private val pricingService = mockk<PricingService>()
    private val vehicleExitService = VehicleExitService(
        paymentRepo,
        sectorRepo,
        sessionRepo,
        spotRepo,
        pricingService,
    )

    @Test
    fun `given a valid exit request should process vehicle exit successfully`() {
        val entryTime = LocalDateTime.parse("2026-03-19T19:37:40").toInstant(ZoneOffset.UTC)
        val exitTime = LocalDateTime.parse("2026-03-19T20:37:40").toInstant(ZoneOffset.UTC)
        val exitRequest = ExitRequest("licensePlate", "2026-03-19T20:37:40", "EXIT")
        val garageSector = GarageSector(1, "A", BigDecimal(10), 10, 1, null, null, null)
        val sectorSpot = SectorSpot(1, "A", 10.0, 20.0, true)
        val parkingSession = ParkingSession(1, "licensePlate", garageSector, sectorSpot, entryTime, null, null, SessionStatus.ACTIVE)

        every { sessionRepo.findByLicensePlateAndStatus(exitRequest.licensePlate, SessionStatus.ACTIVE) } returns parkingSession

        every { sectorRepo.update(any()) } returns null
        every { spotRepo.update(any()) } returns null
        every { sessionRepo.update(any()) } returns null
        every { paymentRepo.save(any()) } returns null

        every { pricingService.calculate(entryTime, exitTime, garageSector.basePrice) } returns BigDecimal(10)

        val result = vehicleExitService.processVehicleExit(exitRequest)
        val webhookResponse = result.body() as WebhookResponse
        val exitResponse = webhookResponse.data!![0] as ExitResponse

        verify(exactly = 1) { sessionRepo.findByLicensePlateAndStatus(exitRequest.licensePlate, SessionStatus.ACTIVE) }
        verify(exactly = 1) { pricingService.calculate(entryTime, exitTime, garageSector.basePrice) }
        verify(exactly = 1) { sectorRepo.update(any()) }
        verify(exactly = 1) { spotRepo.update(any()) }
        verify(exactly = 1) { sessionRepo.update(any()) }
        verify(exactly = 1) { paymentRepo.save(any()) }

        assertEquals(HttpStatus.OK, result.status())
        assertEquals(exitRequest.licensePlate, exitResponse.licensePlate)
        assertEquals(exitTime, exitResponse.exitTime)
        assertEquals(BigDecimal(10), exitResponse.amount)
    }

    @Test
    fun `given a valid exit request should return bad request when parking session is not found`() {
        val exitRequest = ExitRequest("licensePlate", "2026-03-19T20:37:40", "EXIT")

        every { sessionRepo.findByLicensePlateAndStatus(exitRequest.licensePlate, SessionStatus.ACTIVE) } returns null

        val result = vehicleExitService.processVehicleExit(exitRequest)
        val webhookResponse = result.body() as WebhookResponse

        verify(exactly = 1) { sessionRepo.findByLicensePlateAndStatus(exitRequest.licensePlate, SessionStatus.ACTIVE) }

        assertEquals(HttpStatus.BAD_REQUEST, result.status())
        assertEquals("Vehicle is not parked", webhookResponse.error)
    }

    @Test
    fun `given a valid exit request should return bad request when exit time format is invalid`() {
        val entryTime = LocalDateTime.parse("2026-03-19T19:37:40").toInstant(ZoneOffset.UTC)
        val exitRequest = ExitRequest("licensePlate", "2026-03-19 20:37:40", "EXIT")
        val garageSector = GarageSector(1, "A", BigDecimal(10), 10, 1, null, null, null)
        val sectorSpot = SectorSpot(1, "A", 10.0, 20.0, true)
        val parkingSession = ParkingSession(1, "licensePlate", garageSector, sectorSpot, entryTime, null, null, SessionStatus.ACTIVE)

        every { sessionRepo.findByLicensePlateAndStatus(exitRequest.licensePlate, SessionStatus.ACTIVE) } returns parkingSession

        val result = vehicleExitService.processVehicleExit(exitRequest)
        val webhookResponse = result.body() as WebhookResponse

        verify(exactly = 1) { sessionRepo.findByLicensePlateAndStatus(exitRequest.licensePlate, SessionStatus.ACTIVE) }

        assertEquals(HttpStatus.BAD_REQUEST, result.status())
        assertEquals("Invalid exit time format", webhookResponse.error)
    }

}