package com.estapar.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "payments")
data class Payment(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "parking_session_id", nullable = false)
    val session: ParkingSession,

    @Column(nullable = false)
    val amount: BigDecimal,

    val currency: String = "BRL",

    @Column(name = "paid_at")
    val paidAt: Instant = Instant.now()
)
