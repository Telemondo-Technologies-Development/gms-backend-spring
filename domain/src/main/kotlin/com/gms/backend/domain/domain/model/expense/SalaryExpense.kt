package com.gms.backend.domain.domain.model.expense

import com.gms.backend.domain.domain.model.branch.Branch
import com.gms.backend.domain.domain.model.storage.ObjectStorage
import com.gms.backend.domain.domain.model.user.Actor
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "salary_expenses")
class SalaryExpense {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    var id: UUID? = null

    @Column(nullable = false)
    var salaryType: String? = null

    @Column(nullable = false, precision = 10, scale = 2)
    var amount: BigDecimal? = null

    @Column(nullable = false)
    var period: LocalDate? = null

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
    @JoinColumn(name = "actor_id", nullable = false)
    var actor: Actor? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    var createdBy: Actor? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = false)
    var updatedBy: Actor? = null

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "salary_expenses_objects",
        joinColumns = [JoinColumn(name = "salary_expense_id")],
        inverseJoinColumns = [JoinColumn(name = "object_id")],
    )
    var salaryExpensesObjects = mutableSetOf<ObjectStorage>()
}
