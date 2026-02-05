package com.gms.backend.domain.domain.model.asset

import com.gms.backend.domain.domain.model.expense.SuppliesExpense
import com.gms.backend.domain.domain.model.storage.ObjectStorage
import com.gms.backend.domain.domain.model.user.Actor
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

@Entity
@Table(name = "supplies_logs")
class SuppliesLog {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    lateinit var id: UUID

    @Column(nullable = false)
    @field:NotBlank(message = "Name is required")
    lateinit var name: String

    @Column(nullable = false)
    @field:NotNull(message = "Quantity is required")
    var quantity: Int = 0

    @Column
    var remarks: String? = null

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    lateinit var createdAt: Instant

    @UpdateTimestamp
    @Column(nullable = false)
    lateinit var updatedAt: Instant

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplies_id", nullable = false)
    lateinit var supplies: Supply

    @Column(name = "supplies_id", insertable = false, updatable = false)
    var suppliesId: UUID? = null

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

    @OneToMany(mappedBy = "suppliesLog")
    var suppliesLogSuppliesExpenses = mutableSetOf<SuppliesExpense>()

    @ManyToMany
    @JoinTable(
        name = "supplies_logs_objects",
        joinColumns = [JoinColumn(name = "supplies_log_id")],
        inverseJoinColumns = [JoinColumn(name = "object_id")]
    )
    var suppliesLogObjects = mutableSetOf<ObjectStorage>()
}