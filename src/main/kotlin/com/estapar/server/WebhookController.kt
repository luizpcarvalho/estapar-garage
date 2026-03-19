package com.estapar.server

import com.estapar.dto.*
import com.estapar.service.VehicleEntryService
import com.estapar.service.VehicleExitService
import com.estapar.service.VehicleParkingService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import jakarta.validation.Valid
import org.slf4j.LoggerFactory

@Controller("/webhook")
class WebhookController (
    private val vehicleEntryService: VehicleEntryService,
    private val vehicleParkingService: VehicleParkingService,
    private val vehicleExitService: VehicleExitService
) {

    private val logger = LoggerFactory.getLogger(WebhookController::class.java)

    @Post
    fun register(@Body request: EventRequest): HttpResponse<WebhookResponse> {
        logger.info("Webhook event received with event request: $request")
        return when(request) {
            is EntryRequest -> vehicleEntryService.processVehicleEntry(request)
            is ParkingRequest -> vehicleParkingService.processVehicleParking(request)
            is ExitRequest -> vehicleExitService.processVehicleExit(request)
            else -> HttpResponse.badRequest(WebhookResponse(error = "Unknown event type"))
        }
    }

}