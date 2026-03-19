package com.estapar.dto

import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable

@Serdeable
@Introspected
data class WebhookResponse(
    val data: List<Any>? = null,
    val error: String? = null,
)

