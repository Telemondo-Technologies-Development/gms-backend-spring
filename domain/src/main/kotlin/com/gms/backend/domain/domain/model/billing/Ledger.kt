package com.gms.backend.domain.domain.model.billing

import com.gms.backend.domain.domain.model.branch.Branch
import com.gms.backend.domain.domain.model.subscription.SubscriptionAvailed
import com.gms.backend.domain.domain.model.user.Actor
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UuidGenerator
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Entity
@Table(name = "ledgers")
class Ledger {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    lateinit var id: UUID

    @Column(nullable = false)
    lateinit var entryType: String

    @Column(nullable = false)
    lateinit var subscriptionName: String

    @Column(nullable = false)
    lateinit var branchName: String

    @Column(nullable = false)
    lateinit var intervals: String

    @Column(nullable = false)
    var intervalCount: Int = 0

    @Column(nullable = false)
    var gracePeriodDays: Int = 0

    @Column(nullable = false, precision = 10, scale = 2)
    var amount: BigDecimal = BigDecimal.ZERO

    @Column(nullable = false)
    lateinit var paymentMethodName: String

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: Instant? = null

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
    @JoinColumn(name = "invoice_id", nullable = false)
    lateinit var invoice: Invoice

    @Column(name = "invoice_id", insertable = false, updatable = false)
    var invoiceId: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    lateinit var payment: Payment

    @Column(name = "payment_id", insertable = false, updatable = false)
    var paymentId: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    lateinit var createdBy: Actor

    @Column(name = "created_by", insertable = false, updatable = false)
    var createdById: UUID? = null

}
