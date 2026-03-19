package com.estapar.dto

import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import java.time.Instant

@Serdeable
@Introspected
data class EntryResponse(
    val licensePlate: String,
    val entryTime: Instant
)