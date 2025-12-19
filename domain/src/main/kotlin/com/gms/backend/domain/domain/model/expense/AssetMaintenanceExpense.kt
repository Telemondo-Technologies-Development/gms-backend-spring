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
    var id: UUID? = null

    @Column(nullable = false, precision = 10, scale = 2)
    var amount: BigDecimal? = null

    @Column(nullable = false)
    var paidAt: Instant? = null

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
    @JoinColumn(name = "asset_maintenance_id", nullable = false)
    var assetMaintenance: AssetMaintenance? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    var createdBy: Actor? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = false)
    var updatedBy: Actor? = null

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "asset_maintenance_expense_objects",
        joinColumns = [JoinColumn(name = "asset_maintenance_expense_id")],
        inverseJoinColumns = [JoinColumn(name = "object_id")],
    )
    var assetMaintenanceExpensesObjects = mutableSetOf<ObjectStorage>()
}