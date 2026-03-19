package com.estapar.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank

@Serdeable
@Introspected
data class EntryRequest(
    @field:JsonProperty("license_plate")
    val licensePlate: String?,
    @field:JsonProperty("entry_time")
    val entryTime: String?,
    @field:JsonProperty("event_type")
    val eventType: String?
) : EventRequest