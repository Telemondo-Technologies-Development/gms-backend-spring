package com.gms.backend.domain.domain.model.expense

import com.gms.backend.domain.domain.model.asset.AssetMaintenance
import com.gms.backend.domain.domain.model.branch.Branch
import com.gms.backend.domain.domain.model.storage.ObjectStorage
import com.gms.backend.domain.domain.model.user.Actor
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Entity
@Table(name = "asset_maintenance_expenses")
class AssetMaintenanceExpense {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    lateinit var id: UUID

    @Column(nullable = false, precision = 10, scale = 2)
    var amount: BigDecimal = BigDecimal.ZERO

    @Column(nullable = false)
    lateinit var paidAt: Instant

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    lateinit var createdAt: Instant

    @UpdateTimestamp
    @Column(nullable = false)
    lateinit var updatedAt: Instant

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    lateinit var branch: Branch

    @Column(name = "branch_id", insertable = false, updatable = false)
    var branchId: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_maintenance_id", nullable = false)
    lateinit var assetMaintenance: AssetMaintenance

    @Column(name = "asset_maintenance_id", insertable = false, updatable = false)
    var assetMaintenanceId: UUID? = null

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
        name = "asset_maintenance_expense_objects",
        joinColumns = [JoinColumn(name = "asset_maintenance_expense_id")],
        inverseJoinColumns = [JoinColumn(name = "object_id")],
    )
    var assetMaintenanceExpensesObjects = mutableSetOf<ObjectStorage>()
}