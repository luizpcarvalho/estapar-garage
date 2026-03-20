package com.estapar.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank
import java.time.Instant

@Serdeable
@Introspected
data class ExitRequest(
    @field:JsonProperty("license_plate")
    val licensePlate: String,
    @field:JsonProperty("exit_time")
    val exitTime: String,
    @field:JsonProperty("event_type")
    val eventType: String
) : EventRequest