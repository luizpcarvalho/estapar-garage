package com.estapar.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank

@Serdeable
@Introspected
data class RevenueRequest(
    @field:JsonProperty("date")
    @field:NotBlank(message = "The field date must not be null or empty")
    val date: String?,
    @field:JsonProperty("sector")
    @field:NotBlank(message = "The field sector must not be null or empty")
    val sector: String?,
)