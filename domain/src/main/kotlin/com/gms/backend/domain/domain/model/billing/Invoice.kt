package com.gms.backend.domain.domain.model.billing

import com.fasterxml.jackson.annotation.JsonIgnore
import com.gms.backend.domain.domain.model.branch.Branch
import com.gms.backend.domain.domain.model.member.MemberSubscription
import com.gms.backend.domain.domain.model.subscription.SubscriptionAvailed
import com.gms.backend.domain.domain.model.user.Actor
import jakarta.persistence.*
import jakarta.validation.constraints.PositiveOrZero
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Entity
@Table(name = "invoices")
class Invoice {

    enum class InvoiceStatus {
        DRAFT,
        PENDING,
        ISSUED,
        PAID,
        PARTIAL, // Next time
        DUE,
        OVERDUE
    }

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    lateinit var id: UUID

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    lateinit var status: InvoiceStatus

    @Column(nullable = false, precision = 10, scale = 2)
    @PositiveOrZero(message = "Subtotal must not be negative")
    var subtotal: BigDecimal = BigDecimal.ZERO

    @Column(precision = 10, scale = 2)
    var discount: BigDecimal? = null

    @Column(precision = 10, scale = 2)
    var convenienceFee: BigDecimal? = null

    @Column(nullable = false, precision = 10, scale = 2)
    @PositiveOrZero(message = "Total must not be negative")
    var total: BigDecimal = BigDecimal.ZERO

    @Column(nullable = false)
    lateinit var dueDate: Instant

    @Column(nullable = false)
    lateinit var gracePeriodDate: Instant

    @Column(nullable = false)
    lateinit var issuedAt: Instant

    @Column(nullable = false, columnDefinition = "tinyint", length = 1)
    var systemGenerated: Boolean = true

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    lateinit var createdAt: Instant

    @UpdateTimestamp
    @Column(nullable = false)
    lateinit var updatedAt: Instant

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id", nullable = false)
    lateinit var actor: Actor

    @Column(name = "actor_id", insertable = false, updatable = false)
    var actorId: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_availed_id", nullable = false)
    lateinit var subscriptionAvailed: SubscriptionAvailed

    @Column(name = "subscription_availed_id", insertable = false, updatable = false)
    var subscriptionAvailedId: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    lateinit var branch: Branch

    @Column(name = "branch_id", insertable = false, updatable = false)
    var branchId: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_subscription_id", nullable = false)
    lateinit var memberSubscription: MemberSubscription

    @Column(name = "member_subscription_id", insertable = false, updatable = false)
    var memberSubscriptionId: UUID? = null

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

    @OneToMany(mappedBy = "invoice")
    @JsonIgnore
    var invoicePayments = mutableSetOf<Payment>()

    @OneToMany(mappedBy = "invoice")
    @JsonIgnore
    var invoiceLedgers = mutableSetOf<Ledger>()
}
