package com.estapar.service

import com.estapar.dto.RevenueRequest
import com.estapar.dto.RevenueResponse
import com.estapar.entity.GarageSector
import com.estapar.entity.ParkingSession
import com.estapar.entity.Payment
import com.estapar.entity.SessionStatus
import com.estapar.repository.PaymentRepository
import io.micronaut.http.HttpStatus
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

class GarageServiceTest {

    private val paymentRepo = mockk<PaymentRepository>()
    private val garageService = GarageService(paymentRepo)

    @Test
    fun `getRevenue should return the correct revenue for the given date and sector`() {
        val startDate = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC)
        val endDate = LocalDate.now().plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC)

        val garageSector = GarageSector(1, "A", BigDecimal.TEN, 10)
        val parkingSession = ParkingSession(1, "123", garageSector, null, startDate, Instant.now(), null, SessionStatus.COMPLETED)
        val payments = listOf(Payment(1, parkingSession, BigDecimal.TEN))

        every { paymentRepo.findAllByPaidAtBetween(startDate, endDate) } returns payments

        val request = RevenueRequest(LocalDate.now().toString(), "A")
        val result = garageService.getRevenue(request)
        val revenueResponse = result.body() as RevenueResponse

        assertNotNull(result)
        assertEquals(BigDecimal.TEN, revenueResponse.amount)
        assertEquals("BRL", revenueResponse.currency)
    }

    @Test
    fun `getRevenue should return no content when there is no payment data for the given date and sector`() {
        val startDate = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC)
        val endDate = LocalDate.now().plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC)

        every { paymentRepo.findAllByPaidAtBetween(startDate, endDate) } returns null

        val request = RevenueRequest(LocalDate.now().toString(), "A")
        val result = garageService.getRevenue(request)

        assertNotNull(result)
        assertEquals(HttpStatus.NO_CONTENT, result.status)
    }

}