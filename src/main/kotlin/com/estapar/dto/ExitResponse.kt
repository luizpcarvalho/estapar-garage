package com.estapar.dto

import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import java.math.BigDecimal
import java.time.Instant

@Serdeable
@Introspected
data class ExitResponse(
    val licensePlate: String,
    val exitTime: Instant,
    val amount: BigDecimal
)