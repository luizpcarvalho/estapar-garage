package com.estapar.client

import com.estapar.dto.GarageConfigResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client

@Client("http://localhost:3000")
interface SimulatorClient {

    @Get("/garage")
    fun getGarageConfig(): GarageConfigResponse

}