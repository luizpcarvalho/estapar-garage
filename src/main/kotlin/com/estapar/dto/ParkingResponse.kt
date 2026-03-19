package com.estapar.dto

import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import java.time.Instant

@Serdeable
@Introspected
data class ParkingResponse(
    val licensePlate: String,
    val sectorName: String,
    val entryTime: Instant,
)
