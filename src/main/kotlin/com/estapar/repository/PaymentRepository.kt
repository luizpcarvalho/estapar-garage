package com.estapar.repository

import com.estapar.entity.Payment
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import java.time.Instant

@Repository
interface PaymentRepository : CrudRepository<Payment, Long> {

    fun findAllByPaidAtBetween(start: Instant, end: Instant): List<Payment>?

}