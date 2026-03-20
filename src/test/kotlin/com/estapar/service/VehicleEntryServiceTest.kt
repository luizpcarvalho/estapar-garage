package com.estapar.service

import com.estapar.dto.EntryRequest
import com.estapar.dto.EntryResponse
import com.estapar.dto.WebhookResponse
import com.estapar.entity.ParkingSession
import com.estapar.entity.SessionStatus
import com.estapar.repository.ParkingSessionRepository
import io.micronaut.http.HttpStatus
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

class VehicleEntryServiceTest {

    private val sessionRepo = mockk<ParkingSessionRepository>()
    private val service = VehicleEntryService(sessionRepo)

    @Test
    fun `given a valid request should process vehicle entry successfully`() {
        val entryTime = LocalDateTime.parse("2026-03-19T19:37:40").toInstant(ZoneOffset.UTC)
        val request = EntryRequest("licensePlate", "2026-03-19T19:37:40", "ENTRY")

        every { sessionRepo.findByLicensePlateAndStatus(request.licensePlate, SessionStatus.ACTIVE) } returns null
        every { sessionRepo.save(any()) } returns null

        val result = service.processVehicleEntry(request)
        val webhookResponse = result.body() as WebhookResponse

        val entryResponse = webhookResponse.data!![0] as EntryResponse

        verify(exactly = 1) { sessionRepo.findByLicensePlateAndStatus(request.licensePlate, SessionStatus.ACTIVE) }
        verify(exactly = 1) { sessionRepo.save(any()) }
        assertEquals(request.licensePlate, entryResponse.licensePlate)
        assertEquals(entryTime, entryResponse.entryTime)
    }

    @Test
    fun `given a valid request should not process vehicle entry successfully when parking session already exists`() {
        val entryTime = LocalDateTime.parse("2026-03-19T19:37:40").toInstant(ZoneOffset.UTC)
        val request = EntryRequest("licensePlate", "2026-03-19T19:37:40", "ENTRY")
        val session = ParkingSession(1, "licensePlate", null, null, entryTime, null, null, SessionStatus.ACTIVE)

        every { sessionRepo.findByLicensePlateAndStatus(request.licensePlate, SessionStatus.ACTIVE) } returns session

        val result = service.processVehicleEntry(request)
        val webhookResponse = result.body() as WebhookResponse

        verify(exactly = 1) { sessionRepo.findByLicensePlateAndStatus(request.licensePlate, SessionStatus.ACTIVE) }
        verify(exactly = 0) { sessionRepo.save(any()) }
        assertEquals(HttpStatus.BAD_REQUEST, result.status)
        assertEquals("Vehicle already inside parking lot", webhookResponse.error)
    }

    @Test
    fun `given a valid request should not process vehicle entry successfully when entry time format is incorrect`() {
        val request = EntryRequest("licensePlate", "2026-03-19 19:37:40", "ENTRY")

        every { sessionRepo.findByLicensePlateAndStatus(request.licensePlate, SessionStatus.ACTIVE) } returns null

        val result = service.processVehicleEntry(request)
        val webhookResponse = result.body() as WebhookResponse

        verify(exactly = 1) { sessionRepo.findByLicensePlateAndStatus(request.licensePlate, SessionStatus.ACTIVE) }
        verify(exactly = 0) { sessionRepo.save(any()) }
        assertEquals(HttpStatus.BAD_REQUEST, result.status)
        assertEquals("Invalid entry time format", webhookResponse.error)
    }

}