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
    var id: UUID? = null

    @Column(nullable = false)
    var startDate: Instant? = null

    @Column(nullable = false)
    var intervals: String? = null

    @Column(nullable = false)
    var intervalCount: Int? = null

    @OneToMany(mappedBy = "maintenanceSchedule")
    var maintenanceScheduleAssets = mutableSetOf<Asset>()
}
