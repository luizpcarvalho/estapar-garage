package com.estapar.service

import jakarta.inject.Singleton
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Duration
import java.time.Instant

@Singleton
class PricingService {

    fun calculate(entry: Instant, exit: Instant, basePrice: BigDecimal, capacityModifier: Double? = null): BigDecimal {

        val duration = Duration.between(entry, exit)

        val hours = if (duration.toSeconds() <= 1800) {
            0
        } else {
            duration.toHours().coerceAtLeast(1)
        }

        if(capacityModifier != null) {
            val total = basePrice.multiply(BigDecimal(hours))
            return total.multiply(BigDecimal(capacityModifier)).setScale(4, RoundingMode.HALF_UP)
        }

        return if (hours == 1L) basePrice.setScale(4)
        else basePrice.multiply(BigDecimal(hours)).setScale(4, RoundingMode.HALF_UP)
    }
}