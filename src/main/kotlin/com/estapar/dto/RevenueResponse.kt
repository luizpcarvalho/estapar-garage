package com.estapar.dto

import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import java.math.BigDecimal
import java.time.Instant

@Serdeable
@Introspected
data class RevenueResponse(
    val amount: BigDecimal,
    val currency: String,
    val timestamp: Instant,
)