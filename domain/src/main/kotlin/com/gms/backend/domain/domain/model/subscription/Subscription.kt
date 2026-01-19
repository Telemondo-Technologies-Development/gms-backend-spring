package com.gms.backend.domain.domain.model.subscription

import com.gms.backend.domain.domain.model.user.Actor
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Entity
@Table(name = "subscriptions")
class Subscription {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "binary(16)")
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    lateinit var id: UUID

    @Column(nullable = false)
    @NotBlank(message = "Name must not be empty")
    lateinit var name: String

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "Description must not be empty")
    var description: String? = null

    @Column(nullable = false, precision = 10, scale = 2)
    @Positive(message = "Amount must be positive")
    var amount: BigDecimal = BigDecimal.ZERO

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    lateinit var createdAt: Instant

    @UpdateTimestamp
    @Column(nullable = false)
    lateinit var updatedAt: Instant

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_cycle_id", nullable = false)
    lateinit var billingCycle: BillingCycle

    @Column(name = "billing_cycle_id", insertable = false, updatable = false)
    var billingCycleId: UUID? = null

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

    @OneToMany(mappedBy = "subscription")
    var subscriptionSubscriptionAvailed = mutableSetOf<SubscriptionAvailed>()
}
