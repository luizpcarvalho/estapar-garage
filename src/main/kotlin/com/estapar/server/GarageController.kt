package com.estapar.server

import com.estapar.dto.RevenueRequest
import com.estapar.service.GarageService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.validation.Validated
import jakarta.validation.Valid

@Validated
@Controller
class GarageController (private val garageService: GarageService) {

    @Get("/revenue")
    fun getRevenue(@Body @Valid request: RevenueRequest): HttpResponse<Any> {
        return garageService.getRevenue(request)
    }

}