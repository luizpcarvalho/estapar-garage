package com.estapar.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Instant
import java.time.temporal.ChronoUnit

class PricingServiceTest {

    private val pricingService = PricingService()

    @Test
    fun `calculate pricing without capacity modifier when parking duration is less than 30 minutes should return 0`() {
        val result = pricingService.calculate(
            Instant.now(),
            Instant.now().plusSeconds(60),
            BigDecimal(10),
            null
        )
        assertEquals(BigDecimal.ZERO.setScale(4), result)
    }

    @Test
    fun `calculate pricing with capacity modifier when parking duration is less than 30 minutes should return 0`() {
        val result = pricingService.calculate(
            Instant.now(),
            Instant.now().plusSeconds(60),
            BigDecimal(10),
            0.1
        )
        assertEquals(BigDecimal.ZERO.setScale(4), result)
    }

    @Test
    fun `calculate pricing without capacity modifier when parking duration is more than 30 minutes and less than 1 hour should return correct value`() {
        val result = pricingService.calculate(
            Instant.now(),
            Instant.now().plus(45, ChronoUnit.MINUTES),
            BigDecimal(10),
            null
        )
        assertEquals(BigDecimal(10).setScale(4), result)
    }

    @Test
    fun `calculate pricing without capacity modifier when parking duration is more than 1 hour should return correct value`() {
        val result = pricingService.calculate(
            Instant.now(),
            Instant.now().plus(2, ChronoUnit.HOURS),
            BigDecimal(10),
            null
        )
        assertEquals(BigDecimal(20).setScale(4), result)
    }

    @Test
    fun `calculate pricing with capacity modifier when parking duration is more than 30 minutes and less than 1 hour should return correct value`() {
        val result = pricingService.calculate(
            Instant.now(),
            Instant.now().plus(45, ChronoUnit.MINUTES),
            BigDecimal(10),
            1.25
        )
        assertEquals(BigDecimal(12.5).setScale(4), result)
    }

    @Test
    fun `calculate pricing with capacity modifier when parking duration is more than 1 hour should return correct value`() {
        val result = pricingService.calculate(
            Instant.now(),
            Instant.now().plus(2, ChronoUnit.HOURS),
            BigDecimal(10),
            1.1
        )
        assertEquals(BigDecimal(22).setScale(4), result)
    }

}