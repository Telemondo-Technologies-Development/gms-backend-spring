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

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    var id: UUID? = null

    @Column(nullable = false)
    var status: String? = null

    @Column(nullable = true, name = "description")
    var description: String? = null

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: Instant? = null

    @UpdateTimestamp
    @Column(nullable = false)
    var updatedAt: Instant? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    var asset: Asset? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    var createdBy: Actor? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = false)
    var updatedBy: Actor? = null

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "asset_maintenance_objects",
        joinColumns = [JoinColumn(name = "asset_maintenance_id")],
        inverseJoinColumns = [JoinColumn(name = "object_id")],
    )
    var assetMaintenanceObjects = mutableSetOf<ObjectStorage>()

    @OneToMany(mappedBy = "assetMaintenance")
    var assetMaintenanceAssetMaintenanceExpenses = mutableSetOf<AssetMaintenanceExpense>()
}
