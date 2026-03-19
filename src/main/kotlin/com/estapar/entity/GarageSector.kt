package com.estapar.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "sectors")
data class GarageSector(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val sector: String,

    @Column(name = "base_price")
    val basePrice: BigDecimal? = null,

    @Column(name = "max_capacity")
    val maxCapacity: Int? = null,

    @Column(name = "current_occupation", nullable = false)
    val currentOccupation: Int = 0,

    @Column(name = "open_hour", nullable = false)
    val openHour: String? = null,

    @Column(name = "close_hour", nullable = false)
    val closeHour: String? = null,

    @Column(name = "duration_limit_minutes", nullable = false)
    val durationLimitMinutes: Int? = null,

    )
