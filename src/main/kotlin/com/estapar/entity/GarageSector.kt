package com.estapar.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "sectors")
data class GarageSector(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "sector", nullable = false)
    val sector: String,

    @Column(name = "base_price", nullable = false)
    val basePrice: BigDecimal,

    @Column(name = "max_capacity", nullable = false)
    val maxCapacity: Int,

    @Column(name = "current_occupation", nullable = false)
    val currentOccupation: Int = 0,

    @Column(name = "open_hour")
    val openHour: String? = null,

    @Column(name = "close_hour")
    val closeHour: String? = null,

    @Column(name = "duration_limit_minutes")
    val durationLimitMinutes: Int? = null,

    )
