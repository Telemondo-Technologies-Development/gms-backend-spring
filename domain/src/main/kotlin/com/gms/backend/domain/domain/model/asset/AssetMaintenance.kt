package com.gms.backend.domain.domain.model.asset

import com.gms.backend.domain.domain.model.expense.AssetMaintenanceExpense
import com.gms.backend.domain.domain.model.storage.ObjectStorage
import com.gms.backend.domain.domain.model.user.Actor
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

@Entity
@Table(name = "asset_maintenance")
class AssetMaintenance {

    enum class AssetMaintenanceStatus {
        PENDING,
        OVERDUE,
        COMPLETED,
        SKIPPED }

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    lateinit var id: UUID

    @Column(name = "maintenance_date", nullable = false)
    lateinit var maintenanceDate: Instant

    @Column(name = "due_date", nullable = false)
    lateinit var dueDate: Instant

    @Column(name = "completion_date")
    var completionDate: Instant? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    lateinit var status: AssetMaintenanceStatus

    @Column(columnDefinition = "text")
    var description: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    lateinit var asset: Asset

    @Column(name = "asset_id", insertable = false, updatable = false)
    var assetId: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maintenance_schedule_id", nullable = false)
    lateinit var maintenanceSchedule: MaintenanceSchedule

    @Column(name = "maintenance_schedule_id", insertable = false, updatable = false)
    var maintenanceScheduleId: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    lateinit var createdBy: Actor

    @Column(name = "created_by", insertable = false, updatable = false)
    var createdById: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = false)
    lateinit var updatedBy: Actor

    @Column(name = "updated_by", insertable = false, updatable = false)
    var updatedById: UUID? = null

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    lateinit var createdAt: Instant

    @UpdateTimestamp
    @Column(nullable = false)
    lateinit var updatedAt: Instant

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "asset_maintenance_objects",
        joinColumns = [JoinColumn(name = "asset_maintenance_id")],
        inverseJoinColumns = [JoinColumn(name = "object_id")],
    )
    var assetMaintenanceObjects = mutableSetOf<ObjectStorage>()
}