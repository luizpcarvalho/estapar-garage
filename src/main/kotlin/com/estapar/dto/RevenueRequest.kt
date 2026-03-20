package com.estapar.dto

import io.micronaut.core.annotation.Introspected
import io.micronaut.http.annotation.QueryValue
import io.micronaut.serde.annotation.Serdeable

@Serdeable
@Introspected
data class RevenueRequest(
    @field:QueryValue("date")
    val date: String,
    @field:QueryValue("sector")
    val sector: String,
)