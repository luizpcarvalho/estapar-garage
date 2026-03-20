package com.estapar.server

import com.estapar.dto.EntryRequest
import com.estapar.dto.ExitRequest
import com.estapar.dto.ParkingRequest
import com.estapar.service.VehicleEntryService
import com.estapar.service.VehicleExitService
import com.estapar.service.VehicleParkingService
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@MicronautTest
class WebhookControllerTest(@Inject val httpClient: HttpClient) {

    @Inject
    lateinit var vehicleEntryService: VehicleEntryService

    @Inject
    lateinit var vehicleParkingService: VehicleParkingService

    @Inject
    lateinit var vehicleExitService: VehicleExitService

    @Test
    fun `incoming event request with event type ENTRY should call vehicle entry service`() {
        val entryRequest = EntryRequest("licensePlate", "entryTime", "ENTRY")
        every { vehicleEntryService.processVehicleEntry(entryRequest) } returns HttpResponse.ok()

        val eventRequest = """
            {
                "license_plate": "licensePlate",
                "entry_time": "entryTime",
                "event_type": "ENTRY"
            }
        """.trimIndent()

        httpClient.toBlocking().exchange(
            HttpRequest.POST("http://localhost:3003/webhook", eventRequest).contentType(MediaType.APPLICATION_JSON_TYPE),
            String::class.java
        )

        verify(exactly = 1) { vehicleEntryService.processVehicleEntry(entryRequest) }
    }

    @Test
    fun `incoming event request with event type PARKED should call vehicle parking service`() {
        val parkingRequest = ParkingRequest("licensePlate", 10.0, 20.0, "PARKED")
        every { vehicleParkingService.processVehicleParking(parkingRequest) } returns HttpResponse.ok()

        val eventRequest = """
            {
                "license_plate": "licensePlate",
                "lat": 10.0,
                "lng": 20.0,
                "event_type": "PARKED"
            }
        """.trimIndent()

        httpClient.toBlocking().exchange(
            HttpRequest.POST("http://localhost:3003/webhook", eventRequest).contentType(MediaType.APPLICATION_JSON_TYPE),
            String::class.java
        )

        verify(exactly = 1) { vehicleParkingService.processVehicleParking(parkingRequest) }
    }

    @Test
    fun `incoming event request with event type EXIT should call vehicle exit service`() {
        val exitRequest = ExitRequest("licensePlate", "exitTime", "EXIT")
        every { vehicleExitService.processVehicleExit(exitRequest) } returns HttpResponse.ok()

        val eventRequest = """
            {
                "license_plate": "licensePlate",
                "exit_time": "exitTime",
                "event_type": "EXIT"
            }
        """.trimIndent()

        httpClient.toBlocking().exchange(
            HttpRequest.POST("http://localhost:3003/webhook", eventRequest).contentType(MediaType.APPLICATION_JSON_TYPE),
            String::class.java
        )

        verify(exactly = 1) { vehicleExitService.processVehicleExit(exitRequest) }
    }

    @Test
    fun `incoming event request with invalid event type should return bad request`() {
        val eventRequest = """
            {
                "license_plate": "licensePlate",
                "entry_time": "entryTime",
                "event_type": "INVALID",
            }
        """.trimIndent()

        val exception = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(
                HttpRequest.POST("http://localhost:3003/webhook", eventRequest).contentType(MediaType.APPLICATION_JSON_TYPE),
                String::class.java
            )
        }

        verify(exactly = 0) { vehicleEntryService.processVehicleEntry(any()) }
        verify(exactly = 0) { vehicleParkingService.processVehicleParking(any()) }
        verify(exactly = 0) { vehicleExitService.processVehicleExit(any()) }
        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }

    @Singleton
    @Replaces(VehicleEntryService::class)
    fun mockVehicleEntryService(): VehicleEntryService = mockk(relaxed = true)

    @Singleton
    @Replaces(VehicleParkingService::class)
    fun mockVehicleParkingService(): VehicleParkingService = mockk(relaxed = true)

    @Singleton
    @Replaces(VehicleExitService::class)
    fun mockVehicleExitService(): VehicleExitService = mockk(relaxed = true)

}