package com.estapar.service

import com.estapar.dto.RevenueRequest
import com.estapar.dto.RevenueResponse
import com.estapar.repository.PaymentRepository
import io.micronaut.http.HttpResponse
import jakarta.inject.Singleton
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@Singleton
class GarageService (private val paymentRepo: PaymentRepository) {

    fun getRevenue(request: RevenueRequest): HttpResponse<Any> {

        val startDate = LocalDate.parse(request.date ?: "").atStartOfDay().toInstant(ZoneOffset.UTC)
        val endDate = LocalDate.parse(request.date ?: "").plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC)
        val payments = paymentRepo.findAllByPaidAtBetween(startDate, endDate)
            ?: return HttpResponse.noContent()

        val revenue = payments
            .filter { it.session.garageSector?.sector == request.sector }
            .sumOf { it.amount }

        return HttpResponse.ok(RevenueResponse(revenue, "BRL", Instant.now()))

    }

}