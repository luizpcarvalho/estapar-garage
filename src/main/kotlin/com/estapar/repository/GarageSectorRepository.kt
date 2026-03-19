package com.estapar.repository

import com.estapar.entity.GarageSector
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import java.util.Optional

@Repository
interface GarageSectorRepository : CrudRepository<GarageSector, Long> {

    fun findBySector(sector: String): GarageSector?

}