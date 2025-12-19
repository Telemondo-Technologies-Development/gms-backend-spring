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
    var id: UUID? = null

    @Column(nullable = false)
    var entryType: String? = null

    @Column(nullable = false)
    var subscriptionName: String? = null

    @Column(nullable = false)
    var branchName: String? = null

    @Column(nullable = false)
    var intervals: String? = null

    @Column(nullable = false)
    var intervalCount: Int? = null

    @Column(nullable = false)
    var gracePeriodDays: Int? = null

    @Column(nullable = false, precision = 10, scale = 2)
    var amount: BigDecimal? = null

    @Column(nullable = false)
    var paymentMethodName: String? = null

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: Instant? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id", nullable = false)
    var actor: Actor? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_availed_id", nullable = false)
    var subscriptionAvailed: SubscriptionAvailed? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    var branch: Branch? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    var invoice: Invoice? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    var payment: Payment? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    var createdBy: Actor? = null
}
