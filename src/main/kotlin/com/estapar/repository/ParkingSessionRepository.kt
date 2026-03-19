package com.estapar.repository

import com.estapar.entity.ParkingSession
import com.estapar.entity.SessionStatus
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import java.util.Optional

@Repository
interface ParkingSessionRepository : CrudRepository<ParkingSession, Long> {

    fun findByLicensePlateAndStatus(licensePlate: String, status: SessionStatus): ParkingSession?

}