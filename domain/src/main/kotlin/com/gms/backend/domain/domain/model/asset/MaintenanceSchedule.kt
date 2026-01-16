package com.gms.backend.domain.domain.model.asset

import com.gms.backend.domain.domain.model.branch.BranchPersonnel.BranchPersonnelStatus
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

@Entity
@Table(name = "maintenance_schedules")
class MaintenanceSchedule {

    enum class MaintenanceScheduleIntervalUnit {
        DAY,
        WEEK,
        MONTH,
        YEAR
    }

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    lateinit var id: UUID

    @Column(nullable = false)
    lateinit var startDate: Instant

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    lateinit var intervals: MaintenanceScheduleIntervalUnit

    @Column(nullable = false)
    var intervalCount: Int = 0

    @OneToMany(mappedBy = "maintenanceSchedule")
    var maintenanceScheduleAssets = mutableSetOf<Asset>()
}
