package com.estapar.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "parking_sessions")
data class ParkingSession(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "license_plate", nullable = false)
    val licensePlate: String,

    @ManyToOne
    @JoinColumn(name = "garage_sector_id")
    val garageSector: GarageSector? = null,

    @ManyToOne
    @JoinColumn(name = "sector_spot_id")
    val sectorSpot: SectorSpot? = null,

    @Column(name = "entry_time", nullable = false)
    val entryTime: Instant,

    @Column(name = "exit_time")
    val exitTime: Instant? = null,

    @Column(name = "capacity_modifier")
    val capacityModifier: Int? = null,

    @Enumerated(EnumType.STRING)
    val status: SessionStatus = SessionStatus.ACTIVE
)

enum class SessionStatus {
    ACTIVE,
    COMPLETED,
    CANCELLED
}
