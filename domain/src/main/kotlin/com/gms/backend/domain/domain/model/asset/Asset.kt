package com.gms.backend.domain.domain.model.asset

import com.gms.backend.domain.domain.model.asset.AssetMaintenance.AssetMaintenanceStatus
import com.gms.backend.domain.domain.model.branch.Branch
import com.gms.backend.domain.domain.model.storage.ObjectStorage
import com.gms.backend.domain.domain.model.user.Actor
import jakarta.persistence.*
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PastOrPresent
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

@Entity
@Table(name = "assets")
class Asset {

    enum class AssetStatus {
        OPERATIONAL,
        DOWN,
        DECOMMISSIONED
    }

    enum class AssetCondition{
        EXCELLENT,
        GOOD,
        FAIR,
        POOR
    }

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    lateinit var id: UUID

    @Column(nullable = false)
    @field: NotBlank(message = "Name must not be empty")
    lateinit var name: String

    @field:PastOrPresent(message = "Manufactured date cannot be in the future")
    @Column
    var manufacturedDate: Instant? = null

    @Column
    var endOfLife: Instant? = null

    @get:AssertTrue(message = "End of Life date must be after the Manufactured date")
    val isEndOfLifeValid: Boolean
        get() {
            if (manufacturedDate == null || endOfLife == null) return true
            return endOfLife!!.isAfter(manufacturedDate)
        }

    @Column
    var acquisitionDate: Instant? = null

    @get:AssertTrue(message = "Acquisition date must be on or after the manufactured date and before the end of life")
    val isAcquisitionDateValid: Boolean
        get() {
            val currentAcquisition = acquisitionDate ?: return true
            val isAfterManufactured = manufacturedDate?.let { !currentAcquisition.isBefore(it) } ?: true
            val isBeforeEndOfLife = endOfLife?.let { currentAcquisition.isBefore(it) } ?: true

            return isAfterManufactured && isBeforeEndOfLife
        }

    @Enumerated(EnumType.STRING)
    @Column(name = "`condition`", nullable = false)
    lateinit var condition: AssetCondition

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    lateinit var status: AssetStatus

    @Column(columnDefinition = "text")
    var remarks: String? = null

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
    @JoinColumn(name = "asset_category_id", nullable = false)
    lateinit var assetCategory: AssetCategory

    @Column(name = "asset_category_id", insertable = false, updatable = false)
    var assetCategoryId: UUID? = null

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
        name = "asset_objects",
        joinColumns = [JoinColumn(name = "asset_id")],
        inverseJoinColumns = [JoinColumn(name = "object_id")],
    )
    var assetObjects = mutableSetOf<ObjectStorage>()

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "asset_brands",
        joinColumns = [JoinColumn(name = "asset_id")],
        inverseJoinColumns = [JoinColumn(name = "brand_id")],
    )
    var brands = mutableSetOf<Brand>()

    @OneToMany(mappedBy = "asset", cascade = [CascadeType.ALL])
    var maintenanceSchedules = mutableSetOf<MaintenanceSchedule>()

    @OneToMany(mappedBy = "asset")
    var maintenanceLogs = mutableSetOf<AssetMaintenance>()
}