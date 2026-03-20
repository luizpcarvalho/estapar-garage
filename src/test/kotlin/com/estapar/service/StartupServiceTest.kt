package com.estapar.service

import com.estapar.dto.GarageConfigResponse
import com.estapar.dto.GarageResponse
import com.estapar.dto.SpotResponse
import com.estapar.entity.GarageSector
import com.estapar.entity.SectorSpot
import com.estapar.repository.GarageSectorRepository
import com.estapar.repository.SectorSpotRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*

class StartupServiceTest {

    private val sectorRepo = mockk<GarageSectorRepository>()
    private val spotRepo = mockk<SectorSpotRepository>()
    private val startupService = StartupService(sectorRepo, spotRepo)

    private val garageConfigResponse = GarageConfigResponse(
        garage = listOf(GarageResponse("A", BigDecimal(10), 10, "", "", 180)),
        spots = listOf(SpotResponse(1, "A", 25.5, 38.8, false))
    )

    @Test
    fun `should save garageConfig correctly`() {
        every { sectorRepo.findBySector("A") } returns null
        every { sectorRepo.save(any()) } returns null

        every { spotRepo.findById(1) } returns Optional.empty()
        every { spotRepo.save(any()) } returns null

        startupService.saveGarageConfig(garageConfigResponse)

        verify(exactly = 1) { sectorRepo.save(any(GarageSector::class)) }
        verify(exactly = 1) { spotRepo.save(any(SectorSpot::class)) }
    }

    @Test
    fun `should not save garageConfig when it already exists`() {
        val garageSector = GarageSector(1, "A", BigDecimal(10), 10, 0, "", "", 180)
        every { sectorRepo.findBySector("A") } returns garageSector

        val sectorSpot = SectorSpot(1, "A", 25.5, 38.8, false)
        every { spotRepo.findById(1) } returns Optional.of(sectorSpot)

        startupService.saveGarageConfig(garageConfigResponse)

        verify(exactly = 0) { sectorRepo.save(any(GarageSector::class)) }
        verify(exactly = 0) { spotRepo.save(any(SectorSpot::class)) }
    }

}