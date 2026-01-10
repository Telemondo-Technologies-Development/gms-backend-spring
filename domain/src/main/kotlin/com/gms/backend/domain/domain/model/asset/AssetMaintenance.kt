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
        IN,
        OUT,
        UNDECIDED,
    }

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    lateinit var id: UUID

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    lateinit var status: AssetMaintenanceStatus

    @Column(nullable = true, name = "description")
    var description: String? = null

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    lateinit var createdAt: Instant

    @UpdateTimestamp
    @Column(nullable = false)
    lateinit var updatedAt: Instant

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    lateinit var asset: Asset

    @Column(name = "asset_id", insertable = false, updatable = false)
    var assetId: UUID? = null

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
