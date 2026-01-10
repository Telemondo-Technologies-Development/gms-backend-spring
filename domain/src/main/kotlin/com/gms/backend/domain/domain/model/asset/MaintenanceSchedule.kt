package com.gms.backend.domain.domain.model.asset

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

@Entity
@Table(name = "maintenance_schedules")
class MaintenanceSchedule {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    lateinit var id: UUID

    @Column(nullable = false)
    lateinit var startDate: Instant

    @Column(nullable = false)
    lateinit var intervals: String

    @Column(nullable = false)
    var intervalCount: Int = 0

    @OneToMany(mappedBy = "maintenanceSchedule")
    var maintenanceScheduleAssets = mutableSetOf<Asset>()
}
