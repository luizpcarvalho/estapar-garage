package com.estapar.service

import jakarta.inject.Singleton
import java.math.BigDecimal
import java.time.Duration
import java.time.Instant

@Singleton
class PricingService {

    fun calculate(entry: Instant, exit: Instant, basePrice: BigDecimal, capacityModifier: Int? = null): BigDecimal {

        val duration = Duration.between(entry, exit)

        val hours = if (duration.toSeconds() <= 1800) {
            0
        } else {
            duration.toHours().coerceAtLeast(1)
        }

        if(capacityModifier != null) {
            val total = basePrice.multiply(BigDecimal(hours))
            return total.multiply(BigDecimal(capacityModifier / 100))
        }

        return if (hours == 1L) basePrice
        else basePrice.multiply(BigDecimal(hours))
    }
}