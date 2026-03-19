package com.estapar.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import java.math.BigDecimal

@Serdeable
@Introspected
data class GarageConfigResponse(
    @field:JsonProperty("garage")
    val garage: List<GarageResponse>,
    @field:JsonProperty("spots")
    val spots: List<SpotResponse>
)

@Serdeable
@Introspected
data class GarageResponse(
    @field:JsonProperty("sector")
    val sector: String,
    @field:JsonProperty("base_price")
    val basePrice: BigDecimal,
    @field:JsonProperty("max_capacity")
    val maxCapacity: Int,
    @field:JsonProperty("open_hour")
    val openHour: String,
    @field:JsonProperty("close_hour")
    val closeHour: String,
    @field:JsonProperty("duration_limit_minutes")
    val durationLimitMinutes: Int,
)

@Serdeable
@Introspected
data class SpotResponse(
    @field:JsonProperty("id")
    val id: Long,
    @field:JsonProperty("sector")
    val sector: String,
    @field:JsonProperty("lat")
    val lat: Double,
    @field:JsonProperty("lng")
    val lng: Double,
    @field:JsonProperty("occupied")
    val occupied: Boolean,
)
