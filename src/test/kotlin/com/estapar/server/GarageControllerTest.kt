package com.estapar.server

import com.estapar.service.GarageService
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.Test

@MicronautTest
class GarageControllerTest(@Inject val httpClient: HttpClient) {

    @Inject
    lateinit var garageService: GarageService

    @Test
    fun `incoming event request with event type ENTRY should call vehicle entry service`() {
        every { garageService.getRevenue(any()) } returns HttpResponse.ok()

        val request = HttpRequest.GET<Any>("http://localhost:3003/revenue?date=date&sector=A")

        httpClient.toBlocking().exchange(request, String::class.java)

        verify(exactly = 1) { garageService.getRevenue(any()) }
    }

    @Singleton
    @Replaces(GarageService::class)
    fun mockGarageService(): GarageService = mockk(relaxed = true)

}