package com.estapar.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable

@Serdeable
@Introspected
data class ParkingRequest(
    @field:JsonProperty("license_plate")
    val licensePlate: String?,
    @field:JsonProperty("lat")
    val lat: Double,
    @field:JsonProperty("lng")
    val lng: Double,
    @field:JsonProperty("event_type")
    val eventType: String
) : EventRequest