package com.estapar.service

import com.estapar.dto.ExitRequest
import com.estapar.dto.ParkingRequest
import com.estapar.dto.ParkingResponse
import com.estapar.dto.WebhookResponse
import com.estapar.entity.GarageSector
import com.estapar.entity.ParkingSession
import com.estapar.entity.SectorSpot
import com.estapar.entity.SessionStatus
import com.estapar.repository.GarageSectorRepository
import com.estapar.repository.ParkingSessionRepository
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

class VehicleParkingServiceTest {

    private val sectorRepo = mockk<GarageSectorRepository>()
    private val sessionRepo = mockk<ParkingSessionRepository>()
    private val spotRepo = mockk<SectorSpotRepository>()

    private val vehicleParkingService = VehicleParkingService(sectorRepo, sessionRepo, spotRepo)

    @Test
    fun `given a valid request should process vehicle parking successfully`() {
        val entryTime = LocalDateTime.parse("2026-03-19T19:37:40").toInstant(ZoneOffset.UTC)
        val parkingRequest = ParkingRequest("licensePlate", 10.0, 20.0, "PARKED")
        val garageSector = GarageSector(1, "A", BigDecimal(10), 10, 1, null, null, null)
        val sectorSpot = SectorSpot(1, "A", 10.0, 20.0, false)
        val parkingSession = ParkingSession(1, "licensePlate", garageSector, sectorSpot, entryTime, null, null, SessionStatus.ACTIVE)

        every { sessionRepo.findByLicensePlateAndStatus(parkingRequest.licensePlate, SessionStatus.ACTIVE) } returns parkingSession
        every { spotRepo.findByLatAndLng(parkingRequest.lat, parkingRequest.lng) } returns sectorSpot
        every { sectorRepo.findBySector("A") } returns garageSector

        every { spotRepo.update(any()) } returns null
        every { sectorRepo.update(any()) } returns null
        every { sessionRepo.update(any()) } returns null

        val result = vehicleParkingService.processVehicleParking(parkingRequest)
        val webhookResponse = result.body() as WebhookResponse
        val parkingResponse = webhookResponse.data!![0] as ParkingResponse

        verify(exactly = 1) { sessionRepo.findByLicensePlateAndStatus(parkingRequest.licensePlate, SessionStatus.ACTIVE) }
        verify(exactly = 1) { spotRepo.findByLatAndLng(parkingRequest.lat, parkingRequest.lng) }
        verify(exactly = 1) { sectorRepo.findBySector("A") }
        verify(exactly = 1) { spotRepo.update(any()) }
        verify(exactly = 1) { sectorRepo.update(any()) }
        verify(exactly = 1) { sessionRepo.update(any()) }
        assertEquals(HttpStatus.OK, result.status)
        assertEquals("licensePlate", parkingResponse.licensePlate)
        assertEquals("A", parkingResponse.sectorName)
        assertEquals(entryTime, parkingResponse.entryTime)
    }

    @Test
    fun `given a valid request should process vehicle parking successfully with occupation at more than 25 percent`() {
        val entryTime = LocalDateTime.parse("2026-03-19T19:37:40").toInstant(ZoneOffset.UTC)
        val parkingRequest = ParkingRequest("licensePlate", 10.0, 20.0, "PARKED")
        val garageSector = GarageSector(1, "A", BigDecimal(10), 10, 3, null, null, null)
        val sectorSpot = SectorSpot(1, "A", 10.0, 20.0, false)
        val parkingSession = ParkingSession(1, "licensePlate", garageSector, sectorSpot, entryTime, null, null, SessionStatus.ACTIVE)

        every { sessionRepo.findByLicensePlateAndStatus(parkingRequest.licensePlate, SessionStatus.ACTIVE) } returns parkingSession
        every { spotRepo.findByLatAndLng(parkingRequest.lat, parkingRequest.lng) } returns sectorSpot
        every { sectorRepo.findBySector("A") } returns garageSector

        every { spotRepo.update(any()) } returns null
        every { sectorRepo.update(any()) } returns null
        every { sessionRepo.update(any()) } returns null

        val result = vehicleParkingService.processVehicleParking(parkingRequest)
        val webhookResponse = result.body() as WebhookResponse
        val parkingResponse = webhookResponse.data!![0] as ParkingResponse

        verify(exactly = 1) { sessionRepo.findByLicensePlateAndStatus(parkingRequest.licensePlate, SessionStatus.ACTIVE) }
        verify(exactly = 1) { spotRepo.findByLatAndLng(parkingRequest.lat, parkingRequest.lng) }
        verify(exactly = 1) { sectorRepo.findBySector("A") }
        verify(exactly = 1) { spotRepo.update(any()) }
        verify(exactly = 1) { sectorRepo.update(any()) }
        verify(exactly = 1) { sessionRepo.update(any()) }
        assertEquals(HttpStatus.OK, result.status)
        assertEquals("licensePlate", parkingResponse.licensePlate)
        assertEquals("A", parkingResponse.sectorName)
        assertEquals(entryTime, parkingResponse.entryTime)
    }

    @Test
    fun `given a valid request should process vehicle parking successfully with occupation at more than 50 percent`() {
        val entryTime = LocalDateTime.parse("2026-03-19T19:37:40").toInstant(ZoneOffset.UTC)
        val parkingRequest = ParkingRequest("licensePlate", 10.0, 20.0, "PARKED")
        val garageSector = GarageSector(1, "A", BigDecimal(10), 10, 6, null, null, null)
        val sectorSpot = SectorSpot(1, "A", 10.0, 20.0, false)
        val parkingSession = ParkingSession(1, "licensePlate", garageSector, sectorSpot, entryTime, null, null, SessionStatus.ACTIVE)

        every { sessionRepo.findByLicensePlateAndStatus(parkingRequest.licensePlate, SessionStatus.ACTIVE) } returns parkingSession
        every { spotRepo.findByLatAndLng(parkingRequest.lat, parkingRequest.lng) } returns sectorSpot
        every { sectorRepo.findBySector("A") } returns garageSector

        every { spotRepo.update(any()) } returns null
        every { sectorRepo.update(any()) } returns null
        every { sessionRepo.update(any()) } returns null

        val result = vehicleParkingService.processVehicleParking(parkingRequest)
        val webhookResponse = result.body() as WebhookResponse
        val parkingResponse = webhookResponse.data!![0] as ParkingResponse

        verify(exactly = 1) { sessionRepo.findByLicensePlateAndStatus(parkingRequest.licensePlate, SessionStatus.ACTIVE) }
        verify(exactly = 1) { spotRepo.findByLatAndLng(parkingRequest.lat, parkingRequest.lng) }
        verify(exactly = 1) { sectorRepo.findBySector("A") }
        verify(exactly = 1) { spotRepo.update(any()) }
        verify(exactly = 1) { sectorRepo.update(any()) }
        verify(exactly = 1) { sessionRepo.update(any()) }
        assertEquals(HttpStatus.OK, result.status)
        assertEquals("licensePlate", parkingResponse.licensePlate)
        assertEquals("A", parkingResponse.sectorName)
        assertEquals(entryTime, parkingResponse.entryTime)
    }

    @Test
    fun `given a valid request should process vehicle parking successfully with occupation at more than 75 percent`() {
        val entryTime = LocalDateTime.parse("2026-03-19T19:37:40").toInstant(ZoneOffset.UTC)
        val parkingRequest = ParkingRequest("licensePlate", 10.0, 20.0, "PARKED")
        val garageSector = GarageSector(1, "A", BigDecimal(10), 10, 9, null, null, null)
        val sectorSpot = SectorSpot(1, "A", 10.0, 20.0, false)
        val parkingSession = ParkingSession(1, "licensePlate", garageSector, sectorSpot, entryTime, null, null, SessionStatus.ACTIVE)

        every { sessionRepo.findByLicensePlateAndStatus(parkingRequest.licensePlate, SessionStatus.ACTIVE) } returns parkingSession
        every { spotRepo.findByLatAndLng(parkingRequest.lat, parkingRequest.lng) } returns sectorSpot
        every { sectorRepo.findBySector("A") } returns garageSector

        every { spotRepo.update(any()) } returns null
        every { sectorRepo.update(any()) } returns null
        every { sessionRepo.update(any()) } returns null

        val result = vehicleParkingService.processVehicleParking(parkingRequest)
        val webhookResponse = result.body() as WebhookResponse
        val parkingResponse = webhookResponse.data!![0] as ParkingResponse

        verify(exactly = 1) { sessionRepo.findByLicensePlateAndStatus(parkingRequest.licensePlate, SessionStatus.ACTIVE) }
        verify(exactly = 1) { spotRepo.findByLatAndLng(parkingRequest.lat, parkingRequest.lng) }
        verify(exactly = 1) { sectorRepo.findBySector("A") }
        verify(exactly = 1) { spotRepo.update(any()) }
        verify(exactly = 1) { sectorRepo.update(any()) }
        verify(exactly = 1) { sessionRepo.update(any()) }
        assertEquals(HttpStatus.OK, result.status)
        assertEquals("licensePlate", parkingResponse.licensePlate)
        assertEquals("A", parkingResponse.sectorName)
        assertEquals(entryTime, parkingResponse.entryTime)
    }

    @Test
    fun `given a valid request should return bad request when parking session is not found`() {
        val parkingRequest = ParkingRequest("licensePlate", 10.0, 20.0, "PARKED")

        every { sessionRepo.findByLicensePlateAndStatus(parkingRequest.licensePlate, SessionStatus.ACTIVE) } returns null

        val result = vehicleParkingService.processVehicleParking(parkingRequest)
        val webhookResponse = result.body() as WebhookResponse

        verify(exactly = 1) { sessionRepo.findByLicensePlateAndStatus(parkingRequest.licensePlate, SessionStatus.ACTIVE) }
        assertEquals(HttpStatus.BAD_REQUEST, result.status)
        assertEquals("There is no parking session for the vehicle", webhookResponse.error)
    }

    @Test
    fun `given a valid request should return bad request when sector spot is not found`() {
        val entryTime = LocalDateTime.parse("2026-03-19T19:37:40").toInstant(ZoneOffset.UTC)
        val parkingRequest = ParkingRequest("licensePlate", 10.0, 20.0, "PARKED")
        val garageSector = GarageSector(1, "A", BigDecimal(10), 10, 1, null, null, null)
        val sectorSpot = SectorSpot(1, "A", 10.0, 20.0, false)
        val parkingSession = ParkingSession(1, "licensePlate", garageSector, sectorSpot, entryTime, null, null, SessionStatus.ACTIVE)

        every { sessionRepo.findByLicensePlateAndStatus(parkingRequest.licensePlate, SessionStatus.ACTIVE) } returns parkingSession
        every { spotRepo.findByLatAndLng(parkingRequest.lat, parkingRequest.lng) } returns null

        val result = vehicleParkingService.processVehicleParking(parkingRequest)
        val webhookResponse = result.body() as WebhookResponse

        verify(exactly = 1) { sessionRepo.findByLicensePlateAndStatus(parkingRequest.licensePlate, SessionStatus.ACTIVE) }
        verify(exactly = 1) { spotRepo.findByLatAndLng(parkingRequest.lat, parkingRequest.lng) }
        assertEquals(HttpStatus.BAD_REQUEST, result.status)
        assertEquals("Sector spot not found", webhookResponse.error)
    }

    @Test
    fun `given a valid request should return bad request when garage sector is not found`() {
        val entryTime = LocalDateTime.parse("2026-03-19T19:37:40").toInstant(ZoneOffset.UTC)
        val parkingRequest = ParkingRequest("licensePlate", 10.0, 20.0, "PARKED")
        val garageSector = GarageSector(1, "A", BigDecimal(10), 10, 1, null, null, null)
        val sectorSpot = SectorSpot(1, "A", 10.0, 20.0, false)
        val parkingSession = ParkingSession(1, "licensePlate", garageSector, sectorSpot, entryTime, null, null, SessionStatus.ACTIVE)

        every { sessionRepo.findByLicensePlateAndStatus(parkingRequest.licensePlate, SessionStatus.ACTIVE) } returns parkingSession
        every { spotRepo.findByLatAndLng(parkingRequest.lat, parkingRequest.lng) } returns sectorSpot
        every { sectorRepo.findBySector("A") } returns null

        val result = vehicleParkingService.processVehicleParking(parkingRequest)
        val webhookResponse = result.body() as WebhookResponse

        verify(exactly = 1) { sessionRepo.findByLicensePlateAndStatus(parkingRequest.licensePlate, SessionStatus.ACTIVE) }
        verify(exactly = 1) { spotRepo.findByLatAndLng(parkingRequest.lat, parkingRequest.lng) }
        verify(exactly = 1) { sectorRepo.findBySector("A") }
        assertEquals(HttpStatus.BAD_REQUEST, result.status)
        assertEquals("Garage sector not found", webhookResponse.error)
    }

    @Test
    fun `given a valid request should return unprocessable entity when garage sector is at max capacity`() {
        val entryTime = LocalDateTime.parse("2026-03-19T19:37:40").toInstant(ZoneOffset.UTC)
        val parkingRequest = ParkingRequest("licensePlate", 10.0, 20.0, "PARKED")
        val garageSector = GarageSector(1, "A", BigDecimal(10), 10, 10, null, null, null)
        val sectorSpot = SectorSpot(1, "A", 10.0, 20.0, false)
        val parkingSession = ParkingSession(1, "licensePlate", garageSector, sectorSpot, entryTime, null, null, SessionStatus.ACTIVE)

        every { sessionRepo.findByLicensePlateAndStatus(parkingRequest.licensePlate, SessionStatus.ACTIVE) } returns parkingSession
        every { spotRepo.findByLatAndLng(parkingRequest.lat, parkingRequest.lng) } returns sectorSpot
        every { sectorRepo.findBySector("A") } returns garageSector

        every { sessionRepo.update(any()) } returns null

        val result = vehicleParkingService.processVehicleParking(parkingRequest)
        val webhookResponse = result.body() as WebhookResponse

        verify(exactly = 1) { sessionRepo.findByLicensePlateAndStatus(parkingRequest.licensePlate, SessionStatus.ACTIVE) }
        verify(exactly = 1) { spotRepo.findByLatAndLng(parkingRequest.lat, parkingRequest.lng) }
        verify(exactly = 1) { sectorRepo.findBySector("A") }
        verify(exactly = 1) { sessionRepo.update(any()) }
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, result.status)
        assertEquals("Garage sector is full", webhookResponse.error)
    }

    @Test
    fun `given a valid request should return unprocessable entity when sector spot is occupied`() {
        val entryTime = LocalDateTime.parse("2026-03-19T19:37:40").toInstant(ZoneOffset.UTC)
        val parkingRequest = ParkingRequest("licensePlate", 10.0, 20.0, "PARKED")
        val garageSector = GarageSector(1, "A", BigDecimal(10), 10, 1, null, null, null)
        val sectorSpot = SectorSpot(1, "A", 10.0, 20.0, true)
        val parkingSession = ParkingSession(1, "licensePlate", garageSector, sectorSpot, entryTime, null, null, SessionStatus.ACTIVE)

        every { sessionRepo.findByLicensePlateAndStatus(parkingRequest.licensePlate, SessionStatus.ACTIVE) } returns parkingSession
        every { spotRepo.findByLatAndLng(parkingRequest.lat, parkingRequest.lng) } returns sectorSpot
        every { sectorRepo.findBySector("A") } returns garageSector

        every { sessionRepo.update(any()) } returns null

        val result = vehicleParkingService.processVehicleParking(parkingRequest)
        val webhookResponse = result.body() as WebhookResponse

        verify(exactly = 1) { sessionRepo.findByLicensePlateAndStatus(parkingRequest.licensePlate, SessionStatus.ACTIVE) }
        verify(exactly = 1) { spotRepo.findByLatAndLng(parkingRequest.lat, parkingRequest.lng) }
        verify(exactly = 1) { sectorRepo.findBySector("A") }
        verify(exactly = 1) { sessionRepo.update(any()) }
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, result.status)
        assertEquals("Sector spot is occupied", webhookResponse.error)
    }

}