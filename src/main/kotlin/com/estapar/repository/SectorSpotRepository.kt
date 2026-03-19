package com.estapar.repository

import com.estapar.entity.SectorSpot
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface SectorSpotRepository : CrudRepository<SectorSpot, Long> {

    fun findByLatAndLng(lat: Double, lng: Double): SectorSpot?

}