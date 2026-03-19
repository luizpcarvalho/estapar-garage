package com.estapar.entity

import jakarta.persistence.*

@Entity
@Table(name = "sector_spot")
data class SectorSpot(

    @Id
    val id: Long,

    @Column(nullable = false)
    val sector: String,

    @Column(name = "lat", nullable = false)
    val lat: Double,

    @Column(name = "lng", nullable = false)
    val lng: Double,

    @Column(name = "occupied", nullable = false)
    val occupied: Boolean = false,

)
