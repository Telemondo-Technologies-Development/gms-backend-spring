package com.gms.backend.domain.domain.model.asset

import com.gms.backend.domain.domain.model.branch.Branch
import com.gms.backend.domain.domain.model.storage.ObjectStorage
import com.gms.backend.domain.domain.model.user.Actor
import jakarta.persistence.*
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

@Entity
@Table(name = "supplies")
class Supply {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    lateinit var id: UUID

    @Column(nullable = false)
    @field:NotBlank(message = "Name is required")
    lateinit var name: String

    @Column
    var description: String? = null

    @field:Min(value = 0, message = "Quantity cannot be negative")
    @Column(name = "quantity", insertable = false, updatable = false)
    var quantity: Int = 0

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
    @JoinColumn(name = "created_by", nullable = false)
    lateinit var createdBy: Actor

    @Column(name = "created_by", insertable = false, updatable = false)
    var createdById: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = false)
    lateinit var updatedBy: Actor

    @Column(name = "updated_by", insertable = false, updatable = false)
    var updatedById: UUID? = null

    @OneToMany(mappedBy = "supplies")
    var suppliesSuppliesLogs = mutableSetOf<SuppliesLog>()

    @ManyToMany
    @JoinTable(
        name = "supplies_objects",
        joinColumns = [JoinColumn(name = "supplies_id")],
        inverseJoinColumns = [JoinColumn(name = "object_id")]
    )
    var suppliesObjects = mutableSetOf<ObjectStorage>()
}