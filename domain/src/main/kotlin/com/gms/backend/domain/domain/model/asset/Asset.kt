package com.gms.backend.domain.domain.model.asset

import com.gms.backend.domain.domain.model.branch.Branch
import com.gms.backend.domain.domain.model.expense.AssetExpense
import com.gms.backend.domain.domain.model.storage.ObjectStorage
import com.gms.backend.domain.domain.model.user.Actor
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

@Entity
@Table(name = "assets")
class Asset {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    var id: UUID? = null

    @Column(nullable = false)
    var name: String? = null

    @Column
    var manufacturedDate: Instant? = null

    @Column
    var endOfLife: Instant? = null

    @Column(nullable = true)
    var remarks: String? = null

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: Instant? = null

    @UpdateTimestamp
    @Column(nullable = false)
    var updatedAt: Instant? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    var branch: Branch? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maintenance_schedule_id", nullable = false)
    var maintenanceSchedule: MaintenanceSchedule? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_category_id", nullable = false)
    var assetCategory: AssetCategory? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    var createdBy: Actor? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = false)
    var updatedBy: Actor? = null

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "asset_objects",
        joinColumns = [JoinColumn(name = "asset_id")],
        inverseJoinColumns = [JoinColumn(name = "object_id")],
    )
    var assetObjects = mutableSetOf<ObjectStorage>()

    @OneToMany(mappedBy = "asset")
    var assetAssetMaintenances = mutableSetOf<AssetMaintenance>()

    @OneToMany(mappedBy = "asset")
    var assetAssetExpenses = mutableSetOf<AssetExpense>()
}
