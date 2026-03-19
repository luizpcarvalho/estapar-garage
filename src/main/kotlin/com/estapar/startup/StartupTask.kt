package com.estapar.startup

import com.estapar.client.SimulatorClient
import com.estapar.service.StartupService
import io.micronaut.runtime.event.annotation.EventListener
import io.micronaut.runtime.server.event.ServerStartupEvent
import jakarta.inject.Singleton

@Singleton
class StartupTask(
    private val startupService: StartupService,
    private val simulatorClient: SimulatorClient
) {

    @EventListener
    fun onStartup(event: ServerStartupEvent) {
        val garageConfigResponse = simulatorClient.getGarageConfig()
        startupService.saveGarageConfig(garageConfigResponse)
    }
}